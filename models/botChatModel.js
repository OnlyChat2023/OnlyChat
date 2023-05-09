import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  id_user: String,
  name: { type: String, default: '' },
  avatar: { type: String, default: '' },
  chats: [
    {
      message: String,
      time: Date,
      user_id: String,
      seen_user: [
        String,
      ]
    }
  ],
  members: [{
    user_id: String,
    avatar: { type: String, default: '' },
    name: { type: String, default: '' },
    nickname: { type: String, default: '' },
  }],
  options: [],
  update_time: Date
});

const botChat = mongoose.model('chatbot', messageSchema);

export default botChat;