import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: mongoose.Schema.ObjectId,
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