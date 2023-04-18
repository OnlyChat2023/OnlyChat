import express from 'express'
import {addDirectMessage } from '../controllers/directMessageController.js';

const router = express.Router()

router.post('addDirectMessage', addDirectMessage);

export default router