import mongoose, { ObjectId } from 'mongoose';
import { Server } from 'socket.io';
import dotenv from 'dotenv';
import fs from 'fs';
import firebase from './firebase/firebase.js';
import { v4 as uuid } from 'uuid';
import bcrypt from 'bcryptjs';
import { Configuration, OpenAIApi } from 'openai';


const port = process.env.PORT || 5000;

import app from "./app.js";
import globalChat from './models/globalChatModel.js';
import directChat from './models/directChatModel.js';
import groupChat from './models/groupChatModel.js';
import botChat from './models/botChatModel.js';
import User from './models/userModel.js';
import { json } from 'express';
dotenv.config({ path: './config.env' });

let basket = {};
const notification_list = {};

mongoose
    .connect(process.env.DATABASE)
    .then(() => {
        console.log('Connected to DB successfully');
    });

const configuration = new Configuration({
    apiKey: process.env.OPENAI_API_KEY,
});

const openai = new OpenAIApi(configuration);

const server = app.listen(port, async () => {
    // console.log(await bcrypt.hash('yaemiko01', 12));
    // console.log(await bcrypt.hash('kleebunbara01', 12));
    // console.log(await bcrypt.hash('paimon01', 12));
    // console.log(await bcrypt.hash('raidenshogun01', 12));
    console.log(`App is running on port ${port}...`);
});

const saveBase64Image = async (dataString, filename) => {
    const matches = dataString.match(/^data:image\/([A-Za-z-+\/]+);base64,(.+)/), response = {};

    if (matches.length !== 3) {
        return new Error('Invalid input string');
    }

    response.type = matches[1];
    response.data = matches[2];

    fs.writeFile(`${filename}.${response.type}`, response.data, { encoding: 'base64' }, (err) => {

    });

    return `${filename}.${response.type}`;
}

const io = new Server(server);

io.on('connection', (socket) => {

    socket.on("register", function (user) {
        const _user = JSON.parse(user);
        basket[_user._id] = socket.id;
    });

    socket.on('joinRoom', (roomInfo, user) => {
        if (socket.room) {
            socket.leave(socket.room);
        }

        if (socket.channel) {
            socket.channel = null;
        }

        const [roomId, channel] = roomInfo.split('::');
        socket.room = roomId;
        socket.channel = channel;

        socket.user = JSON.parse(user);
        socket.join(roomId);
    });

    socket.on('leaveRoom', () => {
        // socket.room = null;
        socket.leave(socket.room);
        socket.channel = null;
        socket.room = null;
    });

    socket.on('sendStringMessage', async (message, position, user) => {
        const _user = JSON.parse(user);
        const send_user = await User.findById(_user._id)

        // console.log(send_user);

        let messageModal = {};

        if (socket.channel === 'global_chat') {
            messageModal = {
                message: Buffer.from(message, 'utf-8').toString(),
                user_id: send_user._id,
                imges: [],
                avatar: send_user.anonymous_avatar,
                nickname: send_user.nickname,
                time: new Date()
            }

            const GlobalChannel = await globalChat.findOne({ _id: socket.room });

            for (const member of GlobalChannel.members) {

                if (member.user_id === send_user._id) continue;


                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'global_chat');
            }

            GlobalChannel.chats.push(messageModal);
            await GlobalChannel.save();
        }
        else if (socket.channel === 'direct_message') {
            messageModal = {
                message: Buffer.from(message, 'utf-8').toString(),
                user_id: send_user._id,
                imges: [],
                send_user: [],
                time: new Date()
            }

            const DirectMessage = await directChat.findOne({ _id: socket.room });

            for (const member of DirectMessage.members) {
                if (member.user_id.toString() === send_user._id.toString()) continue;

                const memberOptions = DirectMessage.options.find(option => option.user_id === member.user_id);
                if (memberOptions.notify === true) {
                    const getMessage = firebase.messaging();

                    const user = await User.findOne({ _id: member.user_id });

                    if (user.notify.length > 0) {
                        const messages = {
                            data: {
                                name: send_user.nickname,
                                message: send_user.nickname + ": " + Buffer.from(message, 'utf-8').toString()
                            },
                            tokens: user.notify
                        };
                        getMessage.sendMulticast(messages).then((response) => {
                            // Response is a message ID string.
                            // console.log('Successfully sent message:', response);
                        })
                            .catch((error) => {
                                console.log('Error sending message:', error);
                            });
                    }
                }
                const user_ = await User.findOne({ _id: member.user_id })
                user_.directmessage_channel.filter(el => el.message_id === socket.room)[0].show = true
                user_.save()
                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'direct_message');
            }

            DirectMessage.chats.push(messageModal);
            await DirectMessage.save();
        }
        else if (socket.channel === 'group_chat') {
            messageModal = {
                message: Buffer.from(message, 'utf-8').toString(),
                user_id: send_user._id,
                imges: [],
                avatar: send_user.avatar,
                nickname: send_user.nickname,
                send_user: [],
                time: new Date()
            }

            const GroupChat = await groupChat.findOne({ _id: socket.room });

            for (const member of GroupChat.members) {

                if (member.user_id === send_user._id) continue;

                const memberOptions = GroupChat.options.find(option => option.user_id === member.user_id);
                if (memberOptions.notify === true) {
                    const getMessage = firebase.messaging();

                    const user = await User.findOne({ _id: member.user_id });

                    if (user.notify.length > 0) {
                        const messages = {
                            data: {
                                name: send_user.nickname,
                                message: send_user.nickname + ": " + Buffer.from(message, 'utf-8').toString()
                            },
                            tokens: user.notify
                        };

                        getMessage.sendMulticast(messages).then((response) => {
                            // Response is a message ID string.
                            console.log('Successfully sent message:', response);
                        })
                            .catch((error) => {
                                console.log('Error sending message:', error);
                            });
                    }
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'group_chat');
            }

            GroupChat.chats.push(messageModal);
            await GroupChat.save();
        }
        else if (socket.channel === 'bot_chat') {
            messageModal = {
                message: Buffer.from(message, 'utf-8').toString(),
                user_id: send_user._id,
                time: new Date()
            }

            const botID = socket.room;

            const BotChatNew = await botChat.findOne({ _id: botID }).lean();

            const BotChat = await botChat.findOne({ _id: botID });
            BotChat.chats.push(messageModal);

            const BotChatNewest = BotChatNew.chats.sort(function (a, b) {
                return new Date(a.time) - new Date(b.time);
            }).splice(0, Math.min(100, BotChatNew.chats.length));

            const listMessage = BotChatNewest.map(el => ({
                role: (el.user_id === send_user._id) ? 'user' : 'assistant',
                content: el.message
            }));

            const completion = await openai.createChatCompletion({
                model: "gpt-3.5-turbo",
                messages: [...listMessage, { role: "user", content: Buffer.from(message, 'utf-8').toString() }]
            });

            const BotmessageModal = {
                message: completion.data.choices[0].message.content,
                user_id: botID,
                time: new Date()
            }

            BotChat.chats.push(BotmessageModal);

            await BotChat.save();

            io.sockets.in(botID).emit('messageListener', BotmessageModal, position + 1, { });
        }

        io.sockets.in(socket.room).emit('messageListener', messageModal, position, { ...send_user, token: '' });
    });

    socket.on('sendImageMessage', async (images, position, user) => {
        const send_user = JSON.parse(user);

        const image_list = JSON.parse(images);
        const imagePath = [];

        for (let i = 0; i < image_list.length; i++) {
            const image = image_list[i];
            const filename = `assets/chats/${socket.channel}/${uuid()}`;

            const real_filename = await saveBase64Image(image, filename);
            imagePath.push(real_filename.replace('assets/', ''));
        }

        let messageModal = {};

        if (socket.channel === 'global_chat') {
            messageModal = {
                message: '',
                user_id: send_user._id,
                images: imagePath,
                avatar: send_user.anonymous_avatar,
                nickname: send_user.nickname,
                time: new Date()
            }

            const GlobalChannel = await globalChat.findOne({ _id: socket.room });

            for (const member of GlobalChannel.members) {

                if (member.user_id === send_user._id) continue;

                // const getMessage = firebase.messaging();

                // const user = await User.findOne({ _id: member.user_id });

                // if (user.notify) {
                //     const messages = {
                //         data: {
                //             name: send_user.nickname,
                //             message: send_user.nickname + ': Đã gửi hình ảnh'
                //         },
                //         token: user.notify
                //     };

                //     getMessage.send(messages).then((response) => {
                //         // Response is a message ID string.
                //         console.log('Successfully sent message:', response);
                //     })
                //         .catch((error) => {
                //             console.log('Error sending message:', error);
                //         });
                // }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'global_chat');
            }

            GlobalChannel.chats.push(messageModal);
            await GlobalChannel.save();
        }
        else if (socket.channel === 'direct_message') {
            messageModal = {
                message: '',
                user_id: send_user._id,
                images: imagePath,
                send_user: [],
                time: new Date()
            }

            const DirectMessage = await directChat.findOne({ _id: socket.room });

            for (const member of DirectMessage.members) {

                if (member.user_id === send_user._id) continue;

                const memberOptions = DirectMessage.options.find(option => option.user_id === member.user_id);
                if (memberOptions.notify === true) {
                    const getMessage = firebase.messaging();

                    const user = await User.findOne({ _id: member.user_id });

                    if (user.notify.length > 0) {
                        const messages = {
                            data: {
                                name: send_user.nickname,
                                message: send_user.nickname + ': Đã gửi hình ảnh'
                            },
                            tokens: user.notify
                        };

                        getMessage.sendMulticast(messages).then((response) => {
                            // Response is a message ID string.
                            console.log('Successfully sent message:', response);
                        })
                            .catch((error) => {
                                console.log('Error sending message:', error);
                            });
                    }
                }
                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'direct_message');
            }

            DirectMessage.chats.push(messageModal);
            await DirectMessage.save();
        }
        else if (socket.channel === 'group_chat') {
            messageModal = {
                message: '',
                user_id: send_user._id,
                images: imagePath,
                avatar: send_user.avatar,
                nickname: send_user.nickname,
                send_user: [],
                time: new Date()
            }

            const GroupChat = await groupChat.findOne({ _id: socket.room });

            for (const member of GroupChat.members) {

                if (member.user_id === send_user._id) continue;

                const memberOptions = GroupChat.options.find(option => option.user_id === member.user_id);
                if (memberOptions.notify === true) {
                    const getMessage = firebase.messaging();

                    const user = await User.findOne({ _id: member.user_id });

                    if (user.notify.length > 0) {
                        const messages = {
                            data: {
                                name: send_user.nickname,
                                message: send_user.nickname + ': Đã gửi hình ảnh'
                            },
                            tokens: user.notify
                        };

                        getMessage.sendMulticast(messages).then((response) => {
                            // Response is a message ID string.
                            console.log('Successfully sent message:', response);
                        })
                            .catch((error) => {
                                console.log('Error sending message:', error);
                            });
                    }
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, 'group_chat');
            }

            GroupChat.chats.push(messageModal);
            await GroupChat.save();
        }

        io.sockets.in(socket.room).emit('messageListener', messageModal, position, { ...send_user, token: '' });
    });

    socket.on("notifyUpdateMessage", async (lastMessageID) => {
        if (socket.channel && lastMessageID) {
            let newMessageList = null;

            if (socket.channel === 'direct_message')
                newMessageList = await directChat.findOne({ _id: socket.room, 'chats._id': { $gt: new mongoose.Types.ObjectId(lastMessageID) } });

            if (socket.channel === 'global_chat')
                newMessageList = await globalChat.findOne({ _id: socket.room, 'chats._id': { $gt: new mongoose.Types.ObjectId(lastMessageID) } });

            if (socket.channel === 'group_chat')
                newMessageList = await groupChat.findOne({ _id: socket.room, 'chats._id': { $gt: new mongoose.Types.ObjectId(lastMessageID) } });

            if (newMessageList) {
                const start = newMessageList.chats.findIndex((item) => item._id.toString() === lastMessageID);
                // console.log(newMessageList.chats[start]);

                // console.log(lastMessageID);

                for (let i = start + 1; i < newMessageList.chats.length; i++) {
                    socket.emit('messageListener', newMessageList.chats[i], -2, { token: '' });
                }
            }
        }
    });

    socket.on('restriction_friend', (friend) => {

    });

    socket.on('block_friend', (friend) => {

    });

    socket.on('disconnect', () => {

    });

    socket.on('send_add_friend_request', () => {

    });

    socket.on('changeNickname', async (id_1, nickname_1, id_2, nickname_2, chat_id) => {
        // console.log(id_1)
        // console.log(nickname_1)
        // console.log(id_2)
        // console.log(nickname_2)
        // console.log(chat_id)

        let dm = await directChat.findById(chat_id)
        dm.members.filter(el => el.user_id == id_1)[0].nickname = nickname_1
        dm.members.filter(el => el.user_id == id_2)[0].nickname = nickname_2


        io.to(basket[id_1]).emit("waitSetNickname", nickname_1, nickname_2, chat_id);
        io.to(basket[id_2]).emit("waitSetNickname", nickname_2, nickname_1, chat_id);

        await dm.save()
    })

    socket.on('changeGroupName', async (chat_id, newGroupName) => {
        let groupchat = await groupChat.findById(chat_id)
        groupchat.name = newGroupName;

        // console.log(chat_id);

        socket.emit("waitSetGroupName", newGroupName, chat_id);
        await groupchat.save()
    })

    socket.on('acceptRequestAddFriend', async (id, user) => {
        // console.log("acceptRequestAddFriend")
        // console.log(User.)

        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        let f = await User.findOne({ _id: id })


        u.friend.push(id)
        u.friend_request.splice(u.friend_request.indexOf(id), 1)

        f.friend.push(_user._id)

        const new_friends = await User.findOne({ _id: f._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')
        // console.log("2")
        const f_new_friends = await User.findOne({ _id: u._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')
        // console.log("3")
        for (let i of u.directmessage_channel) {
            if (f.directmessage_channel.filter(el => el.message_id === i.message_id).length > 0) {
                io.to(basket[_user._id]).emit('waitAcceptFriend', new_friends);
                io.to(basket[id]).emit("waitAcceptFriend", f_new_friends);

                await u.save()
                await f.save()
                return
            }
        }
        // console.log("4")

        const new_room = {
            chats: [],
            members: [
                {
                    user_id: u._id,
                    nickname: u.name,
                },
                {
                    user_id: f._id,
                    nickname: f.name,
                }
            ],
            options: [
                {
                    user_id: u._id,
                    notify: true,
                    block: false
                },
                {
                    user_id: f._id,
                    notify: true,
                    block: false
                }
            ],
            update_time: new Date
        }

        const direct_message = await directChat.create(new_room);

        u.directmessage_channel.push({
            show: true,
            message_id: direct_message._id.toString()
        })
        f.directmessage_channel.push({
            show: true,
            message_id: direct_message._id.toString()
        })

        for (let j of new_room.members) {
            if (j.user_id === u._id) {
                j.avatar = u.avatar
                j.name = u.name
            }
            else {
                j.avatar = f.avatar
                j.name = f.name
            }
        }

        new_room._id = direct_message._id.toString()
        const u_new_room = new_room
        const f_new_room = new_room

        u_new_room.name = u_new_room.members.filter(el => el.user_id == id)[0].nickname
        u_new_room.avatar = u_new_room.members.filter(el => el.user_id == id)[0].avatar
        u_new_room.options = {
            user_id: u._id,
            notify: false,
            block: false
        }
        // console.log(u_new_room.options)

        io.to(basket[_user._id]).emit('waitAcceptFriend', new_friends, u_new_room);

        f_new_room.name = f_new_room.members.filter(el => el.user_id != id)[0].nickname
        f_new_room.avatar = f_new_room.members.filter(el => el.user_id != id)[0].avatar
        f_new_room.options = {
            user_id: f._id,
            notify: false,
            block: false
        }

        io.to(basket[id]).emit("waitAcceptFriend", f_new_friends, f_new_room);

        await u.save()
        await f.save()
    })

    socket.on('sendRequestAddFriend', async (id, user) => {
        const _user = JSON.parse(user)

        let f = await User.findOne({ _id: id })
        f.friend_request.push(_user._id)

        const friend_requests = await User.findOne({ _id: _user._id }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description -phone')

        io.to(basket[id]).emit("waitRequestAddFriend", friend_requests);
        await f.save()
    })

    socket.on('removeRequestAddFriend', async (id, user) => {
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        u.friend_request.splice(u.friend_request.indexOf(id), 1)
        await u.save()
    })

    socket.on('deleteFriend', async (id, user) => {
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        u.friend.splice(u.friend.indexOf(id), 1)
        let f = await User.findOne({ _id: id })
        f.friend.splice(f.friend.indexOf(_user._id), 1)

        io.to(basket[id]).emit("waitDeleteFriend", _user._id);

        await u.save()
        await f.save()
    })

    socket.on('blockFriend', async (id, user) => {
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        u.friend.splice(u.friend.indexOf(id), 1)
        let f = await User.findOne({ _id: id })
        f.friend.splice(f.friend.indexOf(_user._id), 1)

        io.to(basket[id]).emit("waitDeleteFriend", _user._id);

        u.block.push(id)

        await u.save()
        await f.save()
    })

    socket.on('unblockFriend', async (id, user) => {
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })

        // io.to(basket[id]).emit("waitUnblock", _user._id);

        u.block.splice(u.block.indexOf(id), 1)

        await u.save()
    })

    socket.on("addNewAvatarToServer", async (avtImage, user) => {
        // const userInfo = JSON.parse(user);

        // console.log(avtImage, user);

        const new_filename = await saveBase64Image(avtImage, `assets/avatar/users/${user}`);

        const myUser = await User.findOne({ _id: user });
        // console.log(myUser);
        myUser.avatar = new_filename.replace('assets/', '');
        await myUser.save();

        socket.emit("editDone", new_filename.replace('assets/', ''));
    })
    socket.on('register_notification', (user) => {
        const _user = JSON.parse(user);
        notification_list[_user._id] = socket.id;
    });
    socket.on('updateRoom', async (roomID, Channel) => {
        if (Channel === 'group_chat') {
            const GroupChat = await groupChat.findOne({ _id: roomID });

            for (const member of GroupChat.members) {
                socket.to(basket[member.user_id]).emit('roomListener', roomID, 'group_chat');
            }
        }
        if (Channel === 'global_chat') {
            const GlobalChat = await globalChat.findOne({ _id: roomID });

            for (const member of GlobalChat.members) {
                socket.to(basket[member.user_id]).emit('roomListener', roomID, 'global_chat');
            }
        }
    });
    socket.on('botchat', async (message, position, user) => {
        const completion = await openai.createChatCompletion({
            model: "gpt-3.5-turbo",
            messages: [{role: "user", content: "Hello world"}],
        });
        console.log(completion.data.choices[0].message);
    })
});


process.on('unhandledRejection', (err) => {
    console.log('Unhandled Rejection. Shutting down...');
    console.log(err.name, err.message);
    server.close(() => {
        process.exit(1); // 0 is success, 1 is uncaught exception
    });
});
