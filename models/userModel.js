import mongoose from 'mongoose';

const userSchema = new mongoose.Schema({
  name: { type: String },
  password: { type: String },
  username: { type: String },
  avatar: { type: String },
  email: { type: String },
  phone: { type: String },
  facebook: { type: String },
  instagram: { type: String },
  university: { type: String },
  chatbot_channel: [{ type: String }],
  directmessage_channel: [{
    type: String
  }],
  globalchat_channel: [{
    type: String
  }],
  groupchat_channel: [{
    type: String
  }],
  friend: [{ type: String }],
  friend_request: [{ type: String }]
});

const User = mongoose.model('users', userSchema);

export default User;