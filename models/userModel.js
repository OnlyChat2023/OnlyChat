import mongoose from 'mongoose';
import bcrypt from 'bcryptjs';

const userSchema = new mongoose.Schema({
  name: { type: String },
  password: { type: String },
  username: { type: String },
  nickname: { type: String, default: '' },
  avatar: { type: String, default: '' },
  anonymous_avatar: { type: String, default: '' },
  description: { type: String, default: '' },
  email: { type: String, default: '' },
  phone: { type: String },
  facebook: { type: String, default: '' },
  instagram: { type: String, default: '' },
  university: { type: String, default: '' },
  chatbot_channel: [{
    type: String
  }],
  directmessage_channel: [{
    show: { type: Boolean },
    message_id: { type: String }
  }],
  globalchat_channel: [{
    type: String
  }],
  groupchat_channel: [{
    type: String
  }],
  friend: [{ type: String }],
  friend_request: [{ type: String }],
  block: [{ type: String, default: "" }],
  notify: [{
    type: String,
  }]
});

userSchema.pre('save', async function (next) {
  // Only run this function if password was actually modified
  if (!this.isModified('password')) return next();

  // Hash the password with cost of 12
  this.password = await bcrypt.hash(this.password, 12);

  next();
});

userSchema.methods.correctPassword = async function (candidatePassword, userPassword) {
  return await bcrypt.compare(candidatePassword, userPassword)
}

const User = mongoose.model('users', userSchema);

export default User;