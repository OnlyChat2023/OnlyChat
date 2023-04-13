import express from 'express'
import { updateProfile, getUserInformation, getUserProfile, getDirectChat } from '../controllers/userProfileController.js'

const router = express.Router()

// router.patch('/updateMyPassword', authController.updatePassword);

// Update current user's data
router.patch('/updateProfile', updateProfile);

// Get user information
router.get('/userInformation', getUserInformation);

// get user profile
router.patch('/userProfile', getUserProfile);

router.patch('/directChat', getDirectChat)

export default router;