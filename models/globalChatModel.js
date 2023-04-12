import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: String,
  avatar: String,
  name: String,
  chats: [
    {
      id_: String,
      message: String,
      time: Date,
      user_id: String,
      anonymous_avatar: String,
      nick_name: String,
    }
  ],
  qr_code: String,
  members: [
    {
      user_id: String,
      avatar: String,
      nick_name: String,
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