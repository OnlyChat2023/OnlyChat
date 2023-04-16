import express from 'express'
import { addGroup, getListGroupChat, leaveGroupChat } from '../controllers/groupChatController.js';

const router = express.Router()

router.post('/addGroup', addGroup);
router.post('/getListGroupChat', getListGroupChat);
router.post('/leaveGroupChat', leaveGroupChat);

export default router