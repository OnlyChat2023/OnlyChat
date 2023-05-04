import directChat from "../models/directChatModel.js";
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

const changeOptions = catchAsync(async (req, res, next) => {
    const filterBody = filterObj(req.body, 'user_id', 'notify', 'block');
    filterBody.notify = (req.body.notify === 'true');
    filterBody.block = (req.body.block === 'true');

    const updateDirectMessage = await directChat.updateOne({ _id: req.body.dmc_id, "options.user_id": req.body.user_id }, { $set: { "options.$": filterBody } });

    res.status(200).json({ status: 'success', data: {} });
});

const getBlock = catchAsync(async (req, res, next) => {
    const chat = await directChat.findOne({ _id: req.body.dmc_id });
    var block = undefined;
    for (let i of chat.options) {
        if (i.user_id === req.body.user_id) {
            block = i.block;
            break;
        }
    }

    res.status(200).json({ status: 'success', data: block });
});

const changeNickname = catchAsync(async (req, res, next) => {
    const updateNickname = await directChat.updateOne({ _id: req.body.dmc_id, "members.user_id": req.body.user_id }, { $set: { "members.$.nickname": req.body.nickname } });

    res.status(200).json({ status: 'success', data: {} });
});

const getMetaData = catchAsync(async (req, res, next) => {
    const user = await User.findOne({ _id: req.user.id })
    // console.log(req.user.id)

    const directChats = []
    for (let i of user.directmessage_channel) {
        if (i.show) {
            const dmList = await directChat.findOne({ _id: i.message_id });
            for (let j of dmList.members) {
                if (j.user_id === user._id.toString()) {
                    j.avatar = user.avatar
                    j.name = user.name
                }
                else {
                    const not_me = await User.findOne({ _id: j.user_id })
                    j.avatar = not_me.avatar
                    j.name = not_me.name
                }
            }

            dmList.avatar = dmList.members.filter(el => el.user_id != user._id.toString())[0].avatar
            dmList.name = dmList.members.filter(el => el.user_id != user._id.toString())[0].nickname
            dmList.options = dmList.options.filter(el => el.user_id == user._id.toString());

            const newDM = { ...(dmList.toObject()), _id: dmList._id.toString() };
            newDM.chats = newDM.chats.map(el => ({ ...el, _id: el._id.toString() }));
            newDM.members = newDM.members.map(el => ({ ...el, _id: el._id.toString() }));
            newDM.options = { ...newDM.options[0], _id: newDM.options[0]._id.toString() };

            directChats.push(newDM);
        }
    }

    res.status(200).json({
        status: 'success',
        data: {
            user: user,
            directChat: directChats
        },
    })
});

const deleteRoom = catchAsync(async (req, res) => {
    const user = await User.findOne({ _id: req.user.id })
    user.directmessage_channel.filter(el => el.message_id === req.body.message_id)[0].show = false
    user.save()
    res.status(200).json({
        status: 'success',
    })
})


export { changeOptions, getBlock, changeNickname, getMetaData, deleteRoom }
