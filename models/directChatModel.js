import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  avatar: { type: String, default: '' },
  name: { type: String, default: '' },
  chats: [
    {
      message: String,
      time: Date,
      user_id: String,
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

const directChat = mongoose.model('directmessages', messageSchema);

export default directChat;