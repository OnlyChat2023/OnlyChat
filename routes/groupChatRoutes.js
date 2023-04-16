import express from 'express'
import { addGroup, getListGroupChat } from '../controllers/groupChatController.js';

const router = express.Router()

router.post('/addGroup', addGroup);
router.post('/getListGroupChat', getListGroupChat);

export default router