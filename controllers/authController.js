import catchAsync from '../utils/catchAsync.js';
import AppError from '../utils/appError.js';
import Validator from '../utils/Validator.js';
import REGEX from '../constants/regex.js';
import User from '../models/userModel.js';
import firebase from '../firebase/firebase.js';
import jwt from 'jsonwebtoken';
import { promisify } from 'util';

const signToken = function (id) {
    return jwt.sign({ id: id }, process.env.JWT_SECRET, {
        expiresIn: process.env.JWT_EXPIRES_IN,
    });
};

/*
    AUTH CONTROLLER
    1. login
    2. register
    3. forgot password
    4. reset password
    5. protect
*/

/// > LOGIN
const login = catchAsync(async (req, res, next) => {
    if (!Validator.isValidRequestBody(req.body, ['phonenumber', 'password']))
        return next(new AppError('Bad request', 400));

    const { phonenumber, password } = req.body;

    if (Validator.isEmptyString(phonenumber) || Validator.isEmptyString(password))
        return next(new AppError('Please provide phonenumber, password', 400));
    else if (isNaN(+phonenumber) || !Validator.isMatching(phonenumber, REGEX.PHONE_NUMBER))
        return next(new AppError('Please provide a valid phonenumber', 400));

    const user = await User.findOne({ phone: phonenumber }).select('+password');

    if (!user || !(await user.correctPassword(password, user.password))) {
        return next(new AppError('Incorrect phone or password', 401));
    }

    const token = signToken(user._id);

    const userInfo = {
        info: {
            id: user._id.toString(),
            name: user.name,
            username: user.username,
            email: user.email,
            phone: user.phone,
            facebook: user.facebook,
            instagram: user.instagram,
            description: user.description,
            avatar: user.avatar,
            token: token,
        },
        channel: {
            directMessageChannel: user.directmessage_channel,
            globalChatChannel: user.globalchat_channel,
            groupChatChannel: user.groupchat_channel,
            chatbotChannel: user.chatbot_channel,
        },
    };

    res.status(200).json({
        status: 'success',
        user: {
            ...userInfo,
        },
    });
});

const validateRegister = catchAsync(async (req, res, next) => {
    // Validate request body

    if (!Validator.isValidRequestBody(req.body, ['phonenumber'])) return next(new AppError('Bad request', 400));

    // Validate phonenumber, password and password confirm
    const { phonenumber } = req.body;

    if (Validator.isEmptyString(phonenumber))
        return next(new AppError('Please provide phonenumber, password and password confirm', 400));
    else if (isNaN(+phonenumber) || !Validator.isMatching(phonenumber, REGEX.PHONE_NUMBER))
        return next(new AppError('Please provide a valid phonenumber', 400));

    const founded_user = await User.findOne({ phone: phonenumber });

    if (founded_user) return next(new AppError('This phonenumber has already been registered', 400));

    return res.status(200).json({
        status: 'success',
    });
});

/// > REGISTER
const register = catchAsync(async (req, res, next) => {
    if (
        !Validator.isValidRequestBody(req.body, [
            'username',
            'phonenumber',
            'password',
            'passwordConfirm',
            'firebaseToken',
        ])
    )
        return next(new AppError('Bad request', 400));

    // Validate phonenumber, password and password confirm
    const { username, phonenumber, password, passwordConfirm, firebaseToken } = req.body;

    if (
        Validator.isEmptyString(phonenumber) ||
        Validator.isEmptyString(password) ||
        Validator.isEmptyString(passwordConfirm) ||
        Validator.isEmptyString(firebaseToken)
    )
        return next(new AppError('Please provide phonenumber, password and password confirm', 400));
    else if (isNaN(+phonenumber) || !Validator.isMatching(phonenumber, REGEX.PHONE_NUMBER))
        return next(new AppError('Please provide a valid phonenumber', 400));
    else if (password !== passwordConfirm) return next(new AppError('Password and password confirm do not match', 400));

    const founded_user = await User.findOne({ phone: phonenumber });

    if (founded_user) return next(new AppError('This phonenumber has already been registered', 400));

    const decodedFirebase = await firebase.auth().verifyIdToken(firebaseToken);

    if (phonenumber.substring(1) !== decodedFirebase?.phone_number.substring(3)) {
        return next(new Error('Phone number is invalid with the phone varification', 401));
    }

    const min = 1, max = 25;

    const numberAvt = Math.floor(Math.random() * (max - min + 1) + min);

    const user = await User.create({ username, phone: phonenumber, password, avatar: `avatar/${numberAvt}.png` });

    return res.status(200).json({
        status: 'success',
        message: 'Register successful',
    });
});

/// > FORGOT PASSWORD
const forgot = catchAsync(async (req, res, next) => {
    res.status(200).json({
        status: 'success',
        message: 'Forgot successful',
    });
});

/// > RESET PASSWORD
const reset = catchAsync(async (req, res, next) => {
    res.status(200).json({
        status: 'success',
        message: 'Reset successful',
    });
});

/// > PROTECT API
const protect = catchAsync(async (req, res, next) => {
    // 1) Getting token and check of it's there
    let token = '';
    if (req.headers.authorization && req.headers.authorization.startsWith('Bearer'))
        token = req.headers.authorization.split(' ')[1];

    if (!token)
        return next(new AppError('You are not logged in! Please log in to get access'), 401);

    // 2) Verfication token
    const decoded = await promisify(jwt.verify)(token, process.env.JWT_SECRET);

    // 3) Check if user still exists
    const currentUser = await User.findById(decoded.id);

    if (!currentUser)
        return next(new AppError('The user belonging to this token does no longer exist.', 401));

    // 4) Check if user changed password after the token was issued
    // if (currentUser.changedPasswordAfter(decoded.iat))
    //     return next(new AppError('User recently changed password! Please log in again.'), 401);

    // GRANT ACCESS TO PROTECT ROUTE
    req.user = currentUser;
    next();
});

export default { login, register, forgot, reset, validateRegister, protect };
