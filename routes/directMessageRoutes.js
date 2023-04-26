import express from 'express'
import { addDirectMessage, changeOptions, getBlock, changeNickname, getMetaData } from '../controllers/directMessageController.js';
import authController from '../controllers/authController.js';

const router = express.Router()

router.get('/getListMessage', authController.protect, getMetaData)
router.post('/addDirectMessage', addDirectMessage);
router.post('/changeOptions', changeOptions);
router.post('/getBlock', getBlock);
router.post('/changeNickname', changeNickname);
export default router