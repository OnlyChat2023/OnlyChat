import directChat from "../models/directChatModel.js";
import User from "../models/userModel.js";
import catchAsync from "../utils/catchAsync.js";

const filterObj = (obj, ...allowedFields) => {
    const newObj = {};
    Object.keys(obj).forEach((el) => {
      if (allowedFields.includes(el)) newObj[el] = obj[el];
    });
    return newObj;
  };

const addDirectMessage = catchAsync(async (req, res, next) => {
    const user1 = await User.findOne({_id: req.body.id_1});
    const user2 = await User.findOne({_id: req.body.id_2});

    const newChat = {
        avatar: "",
        name: "",
        chats: [],
        members: [
            {
            user_id: user1._id,
            avatar: user1.avatar,
            name: user1.name,
            nickname: user1.nickname,
            },
            {
                user_id: user2._id,
                avatar: user2.avatar,
                name: user2.name,
                nickname: user2.nickname,
            }
        ],
        options: [
            {
                user_id: user1._id,
                notify: false,
                block: false
            },
            {
                user_id: user2._id,
                notify: false,
                block: false
            }
        ],
        update_time: Date
    }

    const addDM = await directChat.insertMany(newChat, {ordered: true, rawResult: true}, async function(err, result){
        user1.directmessage_channel.push(String(result.insertedIds[0]));
        user2.directmessage_channel.push(String(result.insertedIds[0]));
        await user1.save();
        await user2.save();
  
        res.status(200).json({ status: 'success', data: {} });
      });
});

const changeOptions = catchAsync(async (req, res, next) => {
    const filterBody = filterObj(req.body, 'user_id', 'notify', 'block');
    filterBody.notify = (req.body.notify === 'true');
    filterBody.block = (req.body.block === 'true');
    
    const updateDirectMessage = await directChat.updateOne({_id : req.body.dmc_id, "options.user_id" : req.body.user_id}, {$set : {"options.$": filterBody}});

    res.status(200).json({ status: 'success', data: {}});
});

const getBlock = catchAsync(async (req, res, next) => {
    const chat = await directChat.findOne({_id: req.body.dmc_id});
    var block = undefined;
    for (let i of chat.options){
        if (i.user_id === req.body.user_id){
            block = i.block;
            break;
        }
    }

    res.status(200).json({ status: 'success', data: block});
});

const changeNickname = catchAsync(async (req, res, next) => {
    const updateNickname = await directChat.updateOne({_id : req.body.dmc_id, "members.user_id" : req.body.user_id}, {$set : {"members.$.nickname": req.body.nickname}});

    res.status(200).json({ status: 'success', data: {}});
});

export {addDirectMessage, changeOptions, getBlock, changeNickname}
