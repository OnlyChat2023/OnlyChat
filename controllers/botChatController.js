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

const addGroup = catchAsync(async (req, res, next) => {
  const userInf = await User.findOne({ _id: req.body._id });
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
    options: [],
    update_time: req.body.update_time
  };

  const addBotChat = await botChat.insertMany(filterBody, { ordered: true, rawResult: true }, async function (err, result) {
    userInf.chatbot_channel.push(String(result.insertedIds[0]));
    await userInf.save();

    const newBotChat = await botChat.findById(result.insertedIds[0]);

    newBotChat.members.push({
      user_id: result.insertedIds[0].toString(),
      name: newBotChat.name,
      nickname: newBotChat.name,
      avatar: newBotChat.avatar
    });

    newBotChat.save();
  });

  res.status(200).json({ status: 'success', data: {} });
});

const getMetaData = catchAsync(async (req, res, next) => {
  const user = await User.findOne({ _id: req.user.id })
  // console.log(req.user.id)

  const botChats = []
  for (let i of user.chatbot_channel) {
    const dmList = await botChat.findOne({ _id: i });
    for (let i of dmList.chats) {
      if (dmList.members.filter(el => el.user_id == i.user_id).length != 0) {
        i.avatar = dmList.members.filter(el => el.user_id == i.user_id)[0].avatar
        i.nickname = dmList.members.filter(el => el.user_id == i.user_id)[0].nickname
      }
      else {
        let _u = await User.findOne({ _id: i.user_id })
        i.avatar = _u.avatar
        i.nickname = _u.name
      }
    }

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };

    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = {};

    botChats.push(newDM);
  }

  res.status(200).json({
    status: 'success',
    data: {
      user: user,
      botChat: botChats,
    },
  })
});

export { addGroup, getMetaData }