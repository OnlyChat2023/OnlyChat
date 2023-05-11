import express from 'express'
import { addGroup, getListGroupChat, leaveGroupChat, updateOption, getFriends2AddMember, addMember, getMetaData, deleteRoom } from '../controllers/groupChatController.js';
import authController from '../controllers/authController.js';

const router = express.Router()

router.post('/addGroup', addGroup);
router.post('/getListGroupChat', getListGroupChat);
router.post('/leaveGroupChat', leaveGroupChat);
router.post('/updateNotify', updateOption);
router.post('/getAddMember', getFriends2AddMember);
router.post('/addMember', addMember);
router.get('/getListMessage', authController.protect, getMetaData)
router.post('/deleteRoom', authController.protect, deleteRoom)


export default router