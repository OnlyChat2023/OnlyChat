import mongoose, { ObjectId } from 'mongoose';
import { Server } from 'socket.io';
import dotenv from 'dotenv';
import fs from 'fs';
import firebase from './firebase/firebase.js';
import { v4 as uuid } from 'uuid';
import bcrypt from 'bcryptjs';


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

io.of('/group_message').on('connection', (socket) => {

    socket.on('create_room', (room) => {
        socket.room = room;
        socket.join(room);
    });

    socket.on('add_member', (member) => {
        socket.broadcast.to(socket.room).emit('add_member', member);
    });

    socket.on('remove_member', (member) => {
        socket.broadcast.to(socket.room).emit('remove_member', member);
    });

    socket.on('delete_chat', (room) => {
        // cập nhật csdl
        socket.broadcast.to(room).emit('delete_chat');
    });

    socket.on('seen_message', (room) => {
        socket.room = room;
        socket.broadcast.to(room).emit('seen_message', socket.user);
    });

    socket.on('send_message', (message) => {
        // broadcast toàn bộ client trong room này
        socket.broadcast.to(socket.room).emit('receive_message', {
            message: message,
            user: socket.user,
            time: new Date().getTime()
        });
    });

    socket.on('change_nickname', (user) => {
        // cập nhật csdl
        socket.broadcast.to(socket.room).emit('change_nickname', user);
    });

    socket.on('disconnect', () => {
        console.log('User disconnected');
    });
});

io.of('/group_message').on('connection', (socket) => {

    socket.on('create_room', (room) => {
        socket.room = room;
        socket.join(room);
    });

    socket.on('add_member', (member) => {
        socket.broadcast.to(socket.room).emit('add_member', member);
    });

    socket.on('remove_member', (member) => {
        socket.broadcast.to(socket.room).emit('remove_member', member);
    });

    socket.on('delete_chat', (room) => {
        // cập nhật csdl
        socket.broadcast.to(room).emit('delete_chat');
    });

    socket.on('seen_message', (room) => {
        socket.room = room;
        socket.broadcast.to(room).emit('seen_message', socket.user);
    });

    socket.on('send_message', (message) => {
        // broadcast toàn bộ client trong room này
        socket.broadcast.to(socket.room).emit('receive_message', {
            message: message,
            user: socket.user,
            time: new Date().getTime()
        });
    });

    socket.on('change_nickname', (user) => {
        // cập nhật csdl
        socket.broadcast.to(socket.room).emit('change_nickname', user);
    });

    socket.on('disconnect', () => {
        console.log('User disconnected');
    });
});

io.of('/global_message').on('connection', (socket) => {

    socket.on('create_room', (room) => {
        socket.room = room;
        socket.join(room);
    });

    socket.on('add_member', (member) => {
        socket.broadcast.to(socket.room).emit('add_member', member);
    });

    socket.on('remove_member', (member) => {
        socket.broadcast.to(socket.room).emit('remove_member', member);
    });

    socket.on('delete_chat', (room) => {
        // cập nhật csdl
        socket.broadcast.to(room).emit('delete_chat');
    });

    socket.on('seen_message', (room) => {
        socket.room = room;
        socket.broadcast.to(room).emit('seen_message', socket.user);
    });

    socket.on('send_message', (message) => {
        // broadcast toàn bộ client trong room này
        socket.broadcast.to(socket.room).emit('receive_message', {
            message: message,
            user: socket.user,
            time: new Date().getTime()
        });
    });

    socket.on('set_nickname', (user) => {
        // cập nhật csdl
        socket.emit('set_nickname', user);
    });

    socket.on('disconnect', () => {
        console.log('User disconnected');
    });
});

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
        const send_user = JSON.parse(user);

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

                const getMessage = firebase.messaging();

                const user = await User.findById(member.user_id);

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ": " + Buffer.from(message, 'utf-8').toString()
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
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

                if (member.user_id === send_user._id) continue;

                const getMessage = firebase.messaging();

                const user = await User.findOne({ _id: member.user_id });

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ": " + Buffer.from(message, 'utf-8').toString()
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
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

                const getMessage = firebase.messaging();

                const user = await User.findOne({ _id: member.user_id });

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ": " + Buffer.from(message, 'utf-8').toString()
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
            }

            GroupChat.chats.push(messageModal);
            await GroupChat.save();
        }
        else if (socket.channel === 'bot_chat') {
            messageModal = {
                message: Buffer.from(message, 'utf-8').toString(),
                user_id: send_user._id,
                imges: [],
                avatar: "",
                nickname: "",
                send_user: [],
                time: new Date()
            }

            const BotChat = await botChat.findOne({ _id: socket.room });
            BotChat.chats.push(messageModal);
            await BotChat.save();
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

                const getMessage = firebase.messaging();

                const user = await User.findOne({ _id: member.user_id });

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ': Đã gửi hình ảnh'
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
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

                const getMessage = firebase.messaging();

                const user = await User.findOne({ _id: member.user_id });

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ': Đã gửi hình ảnh'
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
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

                const getMessage = firebase.messaging();

                const user = await User.findOne({ _id: member.user_id });

                if (user.notify) {
                    const messages = {
                        data: {
                            name: send_user.nickname,
                            message: send_user.nickname + ': Đã gửi hình ảnh'
                        },
                        token: user.notify
                    };

                    getMessage.send(messages).then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                    })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                }

                socket.to(basket[member.user_id]).emit('roomListener', socket.room, socket.channel);
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

    socket.on('acceptRequestAddFriend', async (id, user) => {
        // console.log("acceptRequestAddFriend")
        // console.log(User.)
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        u.friend.push(id)
        u.friend_request.splice(u.friend_request.indexOf(id), 1)

        let f = await User.findOne({ _id: id })
        f.friend.push(_user._id)

        // ////////////////////////
        // for (let dm of u.directmessage_channel) {
        //     const DirectMessage = await directChat.findOne({ _id: dm })

        //     for (let i of DirectMessage.members) {
        //         if (i.user_id == f._id) {
        //             res.status(200).json({ status: 'success', data: {} });
        //             return;
        //         }
        //     }
        // }
        // console.log("3")

        // const newChat = {
        //     avatar: "",
        //     name: "",
        //     chats: [],
        //     members: [
        //         {
        //             user_id: u._id,
        //             avatar: u.avatar,
        //             name: u.name,
        //             nickname: u.nickname,
        //         },
        //         {
        //             user_id: f._id,
        //             avatar: f.avatar,
        //             name: f.name,
        //             nickname: f.nickname,
        //         }
        //     ],
        //     options: [
        //         {
        //             user_id: u._id,
        //             notify: false,
        //             block: false
        //         },
        //         {
        //             user_id: f._id,
        //             notify: false,
        //             block: false
        //         }
        //     ],
        //     update_time: Date
        // }
        // console.log("4")

        const list_friends = await User.find({ _id: { $in: u.friend } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')
        io.sockets.emit('acceptRequestListener', list_friends);

        const f_list_friends = await User.find({ _id: { $in: f.friend } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')
        io.to(basket[id]).emit("waitAcceptFriend", f_list_friends);

        await u.save()
        await f.save()
    })

    socket.on('sendRequestAddFriend', async (id, user) => {
        const _user = JSON.parse(user)

        let f = await User.findOne({ _id: id })
        f.friend_request.push(_user._id)

        const list_friend_requests = await User.find({ _id: { $in: f.friend_request } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description -phone')

        io.to(basket[id]).emit("waitRequestAddFriend", list_friend_requests);
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

        const f_list_friends = await User.find({ _id: { $in: f.friend } }).select('-password -username -chatbot_channel -directmessage_channel -globalchat_channel -groupchat_channel -friend -friend_request -anonymous_avatar -email -facebook -instagram -university -nickname -description')
        io.to(basket[id]).emit("waitDeleteFriend", f_list_friends);

        await u.save()
        await f.save()
    })

    socket.on('blockFriend', async (id, user) => {
        const _user = JSON.parse(user)
        let u = await User.findOne({ _id: _user._id })
        u.friend.splice(u.friend.indexOf(id), 1)
        u.block.push(id)
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
                socket.to(basket[member.user_id]).emit('roomListener', roomID, Channel);
            }
        }
        if (Channel === 'global_chat') {
            const GlobalChat = await globalChat.findOne({ _id: roomID });

            for (const member of GlobalChat.members) {
                socket.to(basket[member.user_id]).emit('roomListener', roomID, Channel);
            }
        }
    });
});


process.on('unhandledRejection', (err) => {
    console.log('Unhandled Rejection. Shutting down...');
    console.log(err.name, err.message);
    server.close(() => {
        process.exit(1); // 0 is success, 1 is uncaught exception
    });
});