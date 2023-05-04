import express from 'express'
import { changeOptions, getBlock, changeNickname, getMetaData, deleteRoom } from '../controllers/directMessageController.js';
import authController from '../controllers/authController.js';

const router = express.Router()

router.get('/getListMessage', authController.protect, getMetaData)
router.post('/deleteRoom', authController.protect, deleteRoom)
router.post('/changeOptions', changeOptions);
router.post('/getBlock', getBlock);
router.post('/changeNickname', changeNickname);
export default router