import catchAsync from "../utils/catchAsync.js";
import AppError from "../utils/appError.js";
import Validator from "../utils/Validator.js";
import REGEX from '../constants/regex.js';
// import firebase from '../firebase/firebase.js';


/*
    AUTH CONTROLLER
    1. login
    2. register
    3. forgot password
    4. reset password
*/

/// > LOGIN
const login = catchAsync(async (req, res, next) => {

    res.status(200).json({
        status: "success",
        message: "Login successful"
    });

});

const validateRegister = catchAsync(async (req, res, next) => {
    // Validate request body

    console.log(req.body);

    if (!Validator.isValidRequestBody(req.body, ['phonenumber']))
        return next(new AppError("Bad request", 400)); 

    // Validate phonenumber, password and password confirm
    const { phonenumber } = req.body;

    if (Validator.isEmptyString(phonenumber))
        return next(new AppError("Please provide phonenumber, password and password confirm", 400));

    else if (isNaN(+phonenumber) || !Validator.isMatching(phonenumber, REGEX.PHONE_NUMBER))
        return next(new AppError("Please provide a valid phonenumber", 400));

    const founded_user = await User.findOne({ phone: phonenumber });

    if (founded_user)
        return next(new AppError("This phonenumber has already been registered", 400));

    return res.status(200);
});

/// > REGISTER
const register = catchAsync(async (req, res, next) => {

    if (!Validator.isValidRequestBody(req.body.user, ['phonenumber', 'password', 'passwordConfirm']))
        return next(new AppError("Bad request", 400));

    // Validate phonenumber, password and password confirm
    const { phonenumber, password, passwordConfirm } = req.body.user;

    if (Validator.isEmptyString(phonenumber) || Validator.isEmptyString(password) || Validator.isEmptyString(passwordConfirm))
        return next(new AppError("Please provide phonenumber, password and password confirm", 400));

    else if (isNaN(+phonenumber) || !Validator.isMatching(phonenumber, REGEX.PHONE_NUMBER))
        return next(new AppError("Please provide a valid phonenumber", 400));

    else if (password !== passwordConfirm)
        return next(new AppError("Password and password confirm do not match", 400));

    const founded_user = await User.findOne({ phone: phonenumber });

    if (founded_user)
        return next(new AppError("This phonenumber has already been registered", 400));

    const user = await User.create({ phone: phonenumber, password });

    return res.status(200).json({
        status: "success",
        message: "Register successful"
    });

});

/// > FORGOT PASSWORD
const forgot = catchAsync(async (req, res, next) => {

    res.status(200).json({
        status: "success",
        message: "Forgot successful"
    });

});

/// > RESET PASSWORD
const reset = catchAsync(async (req, res, next) => {

    res.status(200).json({
        status: "success",
        message: "Reset successful"
    });

});

export default { login, register, forgot, reset, validateRegister };