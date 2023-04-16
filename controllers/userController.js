import catchAsync from '../utils/catchAsync.js';
import AppError from '../utils/appError.js';
import User from '../models/userModel.js';

const getListFriend = catchAsync(async (req, res, next) => {

    const list_friends = await User.find({ _id: { $in: req.user.friend } }).select('+_id,+name,+phone,+avatar,+description,+email,+facebook,+instagram,+university');
    const list_friend_requests = await User.find({ _id: { $in: req.user.friend_request } }).select('+_id,+name,+phone,+avatar,+description,+email,+facebook,+instagram,+university');

    res.status(200).json({
        status: 'success',
        data: {
            friends: list_friends,
            friend_requests: list_friend_requests,
        }
    });
});

export default { getListFriend };