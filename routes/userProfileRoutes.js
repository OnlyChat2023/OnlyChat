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

// get user profile
router.patch('/getUserById', getUserById);

export default router;