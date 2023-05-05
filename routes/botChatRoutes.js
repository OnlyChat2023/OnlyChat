import express from 'express'
import {addGroup, getMetaData } from '../controllers/botChatController.js';
import authController from '../controllers/authController.js';

const router = express.Router()

router.post('/addBotChat', addGroup);
router.get('/getListMessage', authController.protect, getMetaData);

export default router