import User from "../models/userModel.js";
import catchAsync from "../utils/catchAsync.js"
import AppError from "../utils/appError.js";
import GlobalChat from "../models/globalChatModel.js"
import DirectChat from "../models/directChatModel.js"
import GroupChat from "../models/groupChatModel.js"
import BotChat from "../models/botChatModel.js"


const getUserInformation = catchAsync(async (req, res) => {
  const user = await User.findOne({ _id: req.user.id })
  // console.log(req.user.id)

  const directChat = []
  for (let i of user.directmessage_channel) {
    const dmList = await DirectChat.findOne({ _id: i });
    dmList.avatar = dmList.members.filter(el => el.user_id != user._id.toString())[0].avatar
    dmList.name = dmList.members.filter(el => el.user_id != user._id.toString())[0].nickname
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = [{ ...newDM.options[0], _id: newDM.options[0]._id.toString() }];

    directChat.push(newDM);
  }
  // console.log(JSON.stringify(directChat, null, 2));

  const groupChat = []
  for (let i of user.groupchat_channel) {
    const dmList = await GroupChat.findOne({ _id: i });
    // console.log(i)
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
    console.log(groupChat);
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = [{ ...newDM.options[0], _id: newDM.options[0]._id.toString() }];

    groupChat.push(newDM);
  }
  // console.log(JSON.stringify(groupChat.members, null, 2));

  const globalChat = []
  for (let i of user.globalchat_channel) {
    const dmList = await GlobalChat.findOne({ _id: i });
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = [{ ...newDM.options[0], _id: newDM.options[0]._id.toString() }];

    globalChat.push(newDM);
  }
  // console.log(JSON.stringify(globalChat.members, null, 2));

  const botChat = []
  for (let i of user.chatbot_channel) {
    // console.log(i)
    const dmList = await BotChat.findOne({ _id: i });
    // console.log(dmList)
    botChat.push(dmList);
  }
  // console.log("Bot", botChat)
  
  res.status(200).json({
    status: 'success',
    data: {
      user: user,
      directChat: directChat,
      groupChat: groupChat,
      globalChat: globalChat,
      botChat: [],
    },
  })
})

const getListFriend = catchAsync(async (req, res, next) => {

  // const list_friends = await User.find({ _id: { $in: req.user.friend } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request')
  const list_friends = await User.find({ _id: { $in: req.user.friend } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')


  const list_friend_requests = await User.find({ _id: { $in: req.user.friend_request } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description -phone')

  res.status(200).json({
    status: 'success',
    data: {
      friends: list_friends,
      friend_requests: list_friend_requests,
    }
  });
});

const getUserById = catchAsync(async (req, res, nex) => {
  const _user = await User.findOne({ _id: req.body._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -anonymous_avatar -nickname').lean()
  let status = 0
  // friend
  if (req.user.friend.includes(_user._id)) status = 1;
  // invite to me
  if (req.user.friend_request.includes(_user._id)) status = 2;
  // send invite
  if (_user.friend_request.includes(req.user.id)) status = 3;

  const newUser = { ..._user, isFriend: status };
  console.log(_user);
  res.status(200).json({
    status: 'success',
    data: newUser
  });
})

const getUserByPhone = catchAsync(async (req, res, nex) => {
  const _user = await User.findOne({ phone: req.body.phone }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -anonymous_avatar -nickname').lean()

  if (_user) {

    let status = 0
    // friend
    if (req.user.friend.includes(_user._id)) status = 1;
    // invite to me
    if (req.user.friend_request.includes(_user._id)) status = 2;
    // send invite
    if (_user.friend_request.includes(req.user.id)) status = 3;

    const newUser = { ..._user, isFriend: status };
    res.status(200).json({
      status: 'success',
      data: newUser
    });
  }
  else {
    res.status(200).json({
      status: '',
      data: {}
    });
  }
})



const filterObj = (obj, ...allowedFields) => {
  const newObj = {};
  Object.keys(obj).forEach((el) => {
    if (allowedFields.includes(el)) newObj[el] = obj[el];
  });
  return newObj;
};

const updateProfile = catchAsync(async (req, res, next) => {
  const _user = await User.findOne({ _id: req.user._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend_request -anonymous_avatar -nickname')
  _user.name = req.body.username;
  _user.email = req.body.email;
  _user.phone = req.body.phone;
  _user.university = req.body.university;
  _user.facebook = req.body.facebook;
  _user.instagram = req.body.instagram;
  _user.description = req.body.description;
  // _user.avatar = req.body.avatar;

  await _user.save();
  console.log(req.body.avatar);
})

export { getUserInformation, updateProfile, getUserById, getListFriend, getUserByPhone }