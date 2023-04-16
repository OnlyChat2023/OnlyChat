import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  avatar: { type: String, default: '' },
  name: String,
  chats: [
    {
      _id: mongoose.Schema.ObjectId,
      message: String,
      time: Date,
      user_id: String,
      nickname: String,
      avatar: { type: String, default: '' },
      seen_user: [
        String
      ]
    }
  ],
  members: [
    {
      user_id: String,
      avatar: { type: String, default: '' },
      name: { type: String, default: '' },
      nickname: { type: String, default: '' },
    }
  ],
  options: [
    {
      user_id: String,
      notify: {
        type: Boolean,
        default: true,
      },
      block: {
        type: Boolean,
        default: false
      }
    }
  ],
  update_time: Date
});

const groupChat = mongoose.model('groupchats', messageSchema);

export default groupChat;