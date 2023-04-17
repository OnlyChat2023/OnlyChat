import express from 'express'
import { updateProfile, getUserInformation, getUserById, getListFriend } from '../controllers/userProfileController.js'
import authController from '../controllers/authController.js';
const router = express.Router()

// router.patch('/updateMyPassword', authController.updatePassword);

// Update current user's data
router.patch('/updateProfile', updateProfile);

// Get user information
router.get('/userInformation', authController.protect, getUserInformation);
router.get('/friends', authController.protect, getListFriend);

router.post('/updateProfile', authController.protect, updateProfile);

// get user profile
router.patch('/getUserById', authController.protect, getUserById);

export default router;