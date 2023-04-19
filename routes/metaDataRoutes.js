import express from 'express'
import { getMetaData } from '../controllers/globalChatController.js'
import { getMetaData as GroupMetaData } from '../controllers/groupChatController.js'
import { getMetaData as DirectMetaData } from '../controllers/directMessageController.js'
import authController from '../controllers/authController.js'
const router = express.Router()

// router.patch('/updateMyPassword', authController.updatePassword);

// Update current user's data
router.get('/globalchat', authController.protect, getMetaData);
router.get('/groupchat', authController.protect, GroupMetaData);
router.get('/directchat', authController.protect, DirectMetaData);

export default router;