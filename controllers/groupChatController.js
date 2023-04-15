import groupChat from "../models/groupChatModel.js";
import User from "../models/userModel.js";
import catchAsync from "../utils/catchAsync.js";

const filterObj = (obj, ...allowedFields) => {
    const newObj = {};
    Object.keys(obj).forEach((el) => {
      if (allowedFields.includes(el)) newObj[el] = obj[el];
    });
    return newObj;
  };

const addGroup = catchAsync(async (req, res, next) =>{
    const userInf = await User.findOne({_id: req.body._id});
    const filterBody = {
      // _id: null, 
      chats: null, 
      members: [{
        user_id: userInf._id,
        name: userInf.name,
        nickname: userInf.nickname,
        avatar: userInf.avatar
      }],
      name: req.body.name,
      avatar: null,
      options:[{
        user_id: userInf._id,
        notify: false,
        block: false,
      }],
      update_time: req.body.update_time
    };

    const addGroupChat = await groupChat.insertMany(filterBody, {ordered: true, rawResult: true}, function(err, res){
      userInf.groupchat_channel.push(String(res.insertedIds[0]));
      userInf.save();
    });
    res.status(200).json({ status: 'success', data: {} });
});

export {addGroup}