import express from 'express'
import { addGroup } from '../controllers/groupChatController.js';

const router = express.Router()

router.post('/addGroup', addGroup);

export default router