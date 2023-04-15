import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  _id: mongoose.Schema.ObjectId,
  id_user: String,
  name: { type: String, default: '' },
  avatar: { type: String, default: '' },
  chats: [
    {
      _id: mongoose.Schema.ObjectId,
      message: String,
      time: Date,
      user_id: String,
      seen_user: [
        String,
      ]
    }
  ],
  members: [{}],
  options: [{}],
  update_time: Date
});

const botChat = mongoose.model('chatbot', messageSchema);

export default botChat;