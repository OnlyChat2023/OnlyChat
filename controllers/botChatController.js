import botChat from "../models/botChatModel.js";
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
      avatar: "avatar/bot.png",
      options:[],
      update_time: req.body.update_time
    };

    const addBotChat = await botChat.insertMany(filterBody, {ordered: true, rawResult: true}, async function(err, result){
      userInf.chatbot_channel.push(String(result.insertedIds[0]));
      await userInf.save();

      res.status(200).json({ status: 'success', data: {} });
    });
});

export {addGroup}