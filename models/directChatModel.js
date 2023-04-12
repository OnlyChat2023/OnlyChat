import mongoose from 'mongoose';
import DateTime from 'node-datetime/src/datetime';

const messageSchema = new mongoose.Schema({
  _id: String,
  chats: [
    {
      id_: String,
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

const directChat = mongoose.model('directmessages', messageSchema);

export default directChat;