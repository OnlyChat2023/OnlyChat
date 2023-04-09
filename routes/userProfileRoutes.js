import express from 'express'
import { updateProfile, userProfile } from '../controllers/userProfileController.js'

const router = express.Router()

// router.patch('/updateMyPassword', authController.updatePassword);

// Update current user's data
router.patch('/updateProfile', updateProfile);

// Get user information
router.get('/userProfile', userProfile);

export default router;