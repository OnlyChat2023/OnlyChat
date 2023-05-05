import express from 'express';
import authController from '../controllers/authController.js';
import catchAsync from '../utils/catchAsync.js';
import User from '../models/userModel.js';

const router = express.Router();

router.patch('/update', authController.protect, catchAsync(async (req, res, next) => {

    const user = await User.findById(req.user.id);

    if (user) {
      if (!user.notify.includes(req.body.token))
      user.notify.push(req.body.token);

      await user.save();
    }
  
    res.status(200).json({
      status: 'success',
      data: {
        message: 'update success'
      }
    })
}));

router.patch('/remove', catchAsync(async (req, res, next) => {

  const user = await User.findOne({ notify: { $in: [req.body.token] } });

  if (user) {
    user.notify = user.notify.filter(el => el != req.body.token);

    await user.save();
  }

  res.status(200).json({
    status: 'success',
    data: {
      message: 'update success'
    }
  })
}));

export default router;