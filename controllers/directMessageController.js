import directChat from "../models/directChatModel.js";
import User from "../models/userModel.js";
import catchAsync from "../utils/catchAsync.js";

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

export {addDirectMessage}
