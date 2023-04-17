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
    newDM.options = [{...newDM.options[0], _id: newDM.options[0]._id.toString()}];

    directChat.push(newDM);
  }
  // console.log("Direct", directChat)

  const groupChat = []
  for (let i of user.groupchat_channel) {
    const dmList = await GroupChat.findOne({ _id: i });

    for (let i of dmList.chats) {
      i.avatar = dmList.members.filter(el => el.user_id == i.user_id)[0].avatar
      i.nickname = dmList.members.filter(el => el.user_id == i.user_id)[0].nickname
    }

    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = [{...newDM.options[0], _id: newDM.options[0]._id.toString()}];

    groupChat.push(newDM);
  }
  // console.log("Group", groupChat)

  const globalChat = []
  for (let i of user.globalchat_channel) {
    const dmList = await GlobalChat.findOne({ _id: i });
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

    const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
    newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
    newDM.options = [{...newDM.options[0], _id: newDM.options[0]._id.toString()}];

    globalChat.push(newDM);
  }
  // console.log("Global", globalChat)

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
  const user = await User.findOne({ _id: req.body._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -nickname')

  res.status(200).json({
    status: 'success',
    data: user
  });
})



const filterObj = (obj, ...allowedFields) => {
  const newObj = {};
  Object.keys(obj).forEach((el) => {
    if (allowedFields.includes(el)) newObj[el] = obj[el];
  });
  return newObj;
};

const updateProfile = catchAsync(async (req, res, next) => {
  const filterBody = filterObj(req.body, 'fullname', 'email', 'phone', 'address', 'gender', 'dob', 'photo');
  if (
    req.body.fullname === '' ||
    req.body.email === '' ||
    req.body.phone === '' ||
    req.body.address === '' ||
    req.body.gender === '' ||
    req.body.dob === ''
  ) {
    return next(new AppError('Thông tin bạn nhập không hợp lệ!', 400));
  }

  // check valid name
  const regex = /^([A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]|[a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ])*(?:[ ][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$/g;
  if (!regex.exec(req.body.fullname)) return next(new AppError('Họ và tên không hợp lệ', 400));

  // check birthday
  const regex_b =
    /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[13-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/g;
  if (!regex_b.exec(req.body.dob)) return next(new AppError('Ngày sinh không hợp lệ', 400));

  // check phone number
  const regex_p = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
  if (!regex_p.exec(req.body.phone)) return next(new AppError('Số điện thoại không hợp lệ', 400));

  // check name & address length
  if (req.body.fullname.length >= 50 || req.body.address.length >= 50)
    return next(new AppError('Thông tin user quá dài. Vui lòng nhập ít hơn 50 kí tự.'));

  // check gender
  const gender = ['Nam', 'Nữ', 'Khác'];
  if (!gender.includes(req.body.gender)) return next(new AppError('Giới tính không tồn tại'));
  // 3) Update user account
  const updatedUser = await User.findByIdAndUpdate("64312f06ab1ff0a59daf4263", filterBody, { new: true, runValidators: true });
  console.log(updatedUser)
  res.status(200).json({ status: 'success', data: { user: updatedUser } });
})

export { getUserInformation, updateProfile, getUserById, getListFriend }