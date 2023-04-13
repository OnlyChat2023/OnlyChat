import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: mongoose.Schema.ObjectId,
  id_user: String,
  name: { type: String, default: '' },
  avatar: { type: String, default: '' },
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