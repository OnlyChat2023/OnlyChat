import express from 'express'
import {addDirectMessage, changeOptions, getBlock, changeNickname } from '../controllers/directMessageController.js';

const router = express.Router()

router.post('/addDirectMessage', addDirectMessage);
router.post('/changeOptions', changeOptions);
router.post('/getBlock', getBlock);
router.post('/changeNickname', changeNickname);
export default router