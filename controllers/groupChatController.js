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

const addGroup = catchAsync(async (req, res, next) => {
  const userInf = await User.findOne({ _id: req.body._id });
  const filterBody = {
    // _id: null, 
    chats: [],
    members: [{
      user_id: userInf._id,
      name: userInf.name,
      nickname: userInf.name,
      avatar: userInf.avatar
    }],
    name: req.body.name,
    avatar: userInf.avatar,
    options: [{
      user_id: userInf._id,
      notify: true,
      block: false,
    }],
    update_time: new Date()
  };

  const addGroupChat = await groupChat.insertMany(filterBody, { ordered: true, rawResult: true }, async function (err, result) {
    userInf.groupchat_channel.push({ message_id: String(result.insertedIds[0]), show: true });
    await userInf.save();

    res.status(200).json({ status: 'success', data: { id: String(result.insertedIds[0]) } });
  });
});

const getListGroupChat = catchAsync(async (req, res, next) => {
  const user = await User.findOne({ _id: req.body._id });

  const GroupChat = []
  for (let i of user.groupchat_channel) {
    const dmList = await groupChat.findOne({ _id: i.message_id });
    dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());
    GroupChat.push(dmList);
  }

  res.status(200).json({ status: 'success', data: GroupChat });
});

const leaveGroupChat = catchAsync(async (req, res, next) => {
  //disconnect user to GroupChat
  const user = await User.findOne({ _id: req.body._id });
  for (let i of user.groupchat_channel) {
    if (i.message_id == req.body.grc_id) {
      user.groupchat_channel.pull(i);
      user.save();
      break;
    }
  }

  //Remove members
  const GroupChat = await groupChat.findOne({ _id: req.body.grc_id });
  for (let i of GroupChat.members) {
    if (i.user_id == req.body._id) {
      GroupChat.members.pull(i);
      break;
    }
  }
  //Remove options
  for (let i of GroupChat.options) {
    if (i.user_id == req.body._id) {
      GroupChat.options.pull(i);
      break;
    }
  }
  GroupChat.save()

  res.status(200).json({ status: 'success', data: {} });
});



const updateOption = catchAsync(async (req, res, next) => {
  const filterBody = filterObj(req.body, 'user_id', 'notify', 'block');
  filterBody.notify = (req.body.notify === 'true');
  filterBody.block = (req.body.block === 'true');

  const updateGroupChat = await groupChat.updateOne({ _id: req.body.grc_id, "options.user_id": req.body.user_id }, { $set: { "options.$": filterBody } });

  res.status(200).json({ status: 'success', data: {} });
});



const getFriends2AddMember = catchAsync(async (req, res, next) => {
  const user = await User.findOne({ _id: req.body.user_id });

  // const alreadyInGroup = await groupChat.findOne({ 'members.user_id': req.body.user_id });
  // if (alreadyInGroup) return res.status(200).json({ status: 'success', data: [] });

  const Group = await groupChat.findOne({ _id: req.body.grc_id });
  const addList = [];

  if (user.friend.length != 0) {
    for (let i of user.friend) {
      const temp = Group.members.find(item => item.user_id === i);
      if (temp) {
        user.friend.pull(i);
      }
    }
  }


  if (user.friend.length != 0) {
    for (let i of user.friend) {
      const fr = await User.findOne({ _id: i });
      const filterBody = {
        "_id": fr._id.toString(),
        "name": fr.name,
        "nickname": fr.nickname,
        "avatar": fr.avatar
      };
      addList.push(filterBody);
    }
  }
  res.status(200).json({ status: 'success', data: addList });
});


const addMember = catchAsync(async (req, res, next) => {
  const Group = await groupChat.findOne({ _id: req.body.grc_id });

  // const alreadyInGroup = await groupChat.findOne({ 'members.user_id': req.body.user_id });
  // if (alreadyInGroup) return res.status(200).json({ status: 'success', data: {} });

  const user = await User.findOne({ _id: req.body.user_id });

  user.groupchat_channel.push({ message_id: req.body.grc_id, show: true });
  Group.members.push({
    "user_id": user._id,
    "name": user.name,
    "nickname": user.name,
    "avatar": user.avatar
  });

  Group.options.push({
    "user_id": user._id,
    "notify": true,
    "block": false
  })

  await user.save();
  await Group.save()
  res.status(200).json({ status: 'success', data: {} });
});

const getMetaData = catchAsync(async (req, res, next) => {
  const user = await User.findOne({ _id: req.user.id })
  // console.log(req.user.id)

  const groupChats = []
  for (let i of user.groupchat_channel) {
    if (i.show) {
      const dmList = await groupChat.findOne({ _id: i.message_id });
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

      dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

      const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
      newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
      newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
      newDM.options = { ...newDM.options[0], _id: newDM.options[0]._id.toString() };

      groupChats.push(newDM);
    }
  }

  res.status(200).json({
    status: 'success',
    data: {
      user: user,
      groupChat: groupChats.sort((a, b) => { return b.update_time - a.update_time }),
    },
  })
});

const deleteRoom = catchAsync(async (req, res) => {
  const user = await User.findOne({ _id: req.user.id })
  user.groupchat_channel.filter(el => el.message_id === req.body.room_id)[0].show = false
  user.save()
  res.status(200).json({
    status: 'success',
  })
})

export { addGroup, getListGroupChat, leaveGroupChat, updateOption, getFriends2AddMember, addMember, getMetaData, deleteRoom }