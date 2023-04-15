import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: mongoose.Schema.ObjectId,
  avatar: { type: String, default: '' },
  name: { type: String, default: '' },
  chats: [
    {
      _id: mongoose.Schema.ObjectId,
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
      avatar: String,
      name: String,
      nickname: String,
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