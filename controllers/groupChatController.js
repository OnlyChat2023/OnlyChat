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
      avatar: userInf.avatar,
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

const leaveGroupChat = catchAsync(async (req, res, next) => {
  //disconnect user to GroupChat
  const user = await User.findOne({_id : req.body._id});
  for(let i of user.groupchat_channel){
    if (i == req.body.grc_id){
      user.groupchat_channel.pull(i);
      user.save();
      break;
    }
  }

  //Remove members
  const GroupChat = await groupChat.findOne({_id: req.body.grc_id});
  for (let i of GroupChat.members){
    if (i.user_id == req.body._id){
      GroupChat.members.pull(i);
      break;
    }
  }
  //Remove options
  for (let i of GroupChat.options){
    if (i.user_id == req.body._id){
      GroupChat.options.pull(i);
      break;
    }
  }
  GroupChat.save()

  res.status(200).json({ status: 'success', data: {}});
});



const updateOption = catchAsync(async (req, res, next) => {
  const filterBody = filterObj(req.body, 'user_id', 'notify', 'block');
  filterBody.notify = (req.body.notify === 'true');
  filterBody.block = (req.body.block === 'true');
  
  const updateGroupChat = await groupChat.updateOne({_id : req.body.grc_id, "options.user_id" : req.body.user_id}, {$set : {"options.$": filterBody}});

  res.status(200).json({ status: 'success', data: {}});
});



const getFriends2AddMember = catchAsync(async (req, res, next) => {
  const user = await User.findOne({_id: req.body.user_id});
  const Group = await groupChat.findOne({_id: req.body.grc_id});
  const addList = [];

  if(user.friend.length != 0){
    for (let i of user.friend){
      const temp = Group.members.find(item => item.user_id === i);
      if (temp){
        user.friend.pull(i);
      }
    }
  }
  
  if (user.friend.length != 0){
    for (let i of user.friend){
      const fr = await User.findOne({_id: i});
      const filterBody = {
          "_id" : fr._id.toString(),
          "name": fr.name,
          "nickname": fr.nickname,
          "avatar": fr.avatar
        };
      addList.push(filterBody);
    }
  }
  res.status(200).json({ status: 'success', data: addList});
});



const addMember = catchAsync(async (req, res, next) => {
  const Group = await groupChat.findOne({_id: req.body.grc_id});
  const user = await User.findOne({_id: req.body.user_id});

  user.groupchat_channel.push(req.body.grc_id);
  Group.members.push({
    "user_id": req.body.user_id,
    "name": req.body.name,
    "nickname": req.body.nickname,
    "avatar": req.body.avatar
  });

  Group.options.push({
    "user_id": req.body.user_id,
    "notify": false,
    "block": false
  })

  await user.save();
  await Group.save()
  res.status(200).json({ status: 'success', data: {}});
});

export {addGroup, getListGroupChat, leaveGroupChat, updateOption, getFriends2AddMember, addMember}