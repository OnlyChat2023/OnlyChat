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
      chats: [], 
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

    const addGroupChat = await groupChat.insertMany(filterBody, {ordered: true, rawResult: true}, async function(err, result){
      userInf.groupchat_channel.push(String(result.insertedIds[0]));
      await userInf.save();

      res.status(200).json({ status: 'success', data: {} });
    });
});

const getListGroupChat = catchAsync(async (req, res, next) => {
  const user = await User.findOne({_id : req.body._id});

  const GroupChat = []
  for (let i of user.groupchat_channel) {
    const dmList = await groupChat.findOne({ _id: i });
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());
    GroupChat.push(dmList);
  }
  
  res.status(200).json({ status: 'success', data: GroupChat });
});

export {addGroup, getListGroupChat}