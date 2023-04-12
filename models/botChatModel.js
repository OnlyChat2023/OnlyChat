import mongoose from 'mongoose';
import DateTime from 'node-datetime/src/datetime';

const messageSchema = new mongoose.Schema({
  _id: String,
  id_user: String,
  name: String,
  chats: [
    {
      message: String,
      isMe: Boolean,
      time: Date,
    }
  ],
  update_time: Date
});

const botChat = mongoose.model('chatbot', messageSchema);

export default botChat;