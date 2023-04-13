import express from 'express'
import { getAll } from '../controllers/messageController.js';

const router = express.Router()

// router.patch('/updateMyPassword', authController.updatePassword);

// Get user information
router.get('/messages', getAll);

export default router;