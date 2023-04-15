import mongoose from 'mongoose';
import { Server } from 'socket.io';
import dotenv from 'dotenv';
import fs from 'fs';
import { v4 as uuid } from 'uuid';


const port = process.env.PORT || 5000;

import app from "./app.js";
dotenv.config({ path: './config.env' });

mongoose
    .connect(process.env.DATABASE)
    .then(() => {
        console.log('Connected to DB successfully');
    });

const server = app.listen(port, () => {
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

    socket.on('joinRoom', (roomInfo, user) => {
        const [roomId, channel] = roomInfo.split('::');
        socket.room = roomId;
        socket.channel = channel;

        socket.user = JSON.parse(user);
        socket.join(roomId);
    });

    socket.on('sendStringMessage', (message, user) => {
        const send_user = JSON.parse(user);

        console.log(message);

        let messageModal = {};

        if (socket.channel === 'global_chat') {
            messageModal = {
                message: message,
                user_id: send_user.id,
                // anonymous_avatar: user.anonymous_avatar,
                anonymous_avatar: '',
                nickname: send_user.name,
                time: new Date()
            }
        }

        io.sockets.in(socket.room).emit('messageListener', messageModal, { ...send_user, token: '' });
    });

    socket.on('sendImageMessage', (images, user) => {
        const send_user = JSON.parse(user);

        const image_list = JSON.parse(images);

        for (let i = 0; i < image_list.length; i++) {
            const image = image_list[i];
            const filename = `assets/chats/${socket.channel}/${uuid()}`;

            saveBase64Image(image, filename);
        }

        let messageModal = {};

        // if (socket.channel === 'global_chat') {
        //     messageModal = {
        //         message: message,
        //         user_id: send_user.id,
        //         // anonymous_avatar: user.anonymous_avatar,
        //         anonymous_avatar: '',
        //         nickname: send_user.name,
        //         time: new Date()
        //     }
        // }

        // io.sockets.in(socket.room).emit('messageListener', messageModal, { ...send_user, token: '' });
    });

    socket.on('add_friend', (friend) => {

    });

    socket.on('remove_friend', (friend) => {

    });

    socket.on('restriction_friend', (friend) => {

    });

    socket.on('block_friend', (friend) => {

    });

    socket.on('disconnect', () => {

    });

    socket.on('send_add_friend_request', () => {

    });
});

process.on('unhandledRejection', (err) => {
    console.log('Unhandled Rejection. Shutting down...');
    console.log(err.name, err.message);
    server.close(() => {
        process.exit(1); // 0 is success, 1 is uncaught exception
    });
});
