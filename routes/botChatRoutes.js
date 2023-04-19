import express from 'express'
import {addGroup } from '../controllers/botChatController.js';

const router = express.Router()

router.post('/addBotChat', addGroup);

export default router