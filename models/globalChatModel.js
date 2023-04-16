import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: mongoose.Schema.ObjectId,
  avatar: { type: String, default: '' },
  name: String,
  chats: [
    {
      _id: mongoose.Schema.ObjectId,
      message: String,
      images: [{
        type: String,
        default: ''
      }],
      time: Date,
      user_id: String,
      anonymous_avatar: String,
      nickname: String,
    }
  ],
  qr_code: String,
  members: [
    {
      user_id: String,
      avatar: String,
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

const globalChat = mongoose.model('globalchats', messageSchema);

export default globalChat;