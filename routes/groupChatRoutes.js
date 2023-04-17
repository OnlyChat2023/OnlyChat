import express from 'express'
import { addGroup, getListGroupChat, leaveGroupChat, updateOption, getFriends2AddMember, addMember} from '../controllers/groupChatController.js';

const router = express.Router()

router.post('/addGroup', addGroup);
router.post('/getListGroupChat', getListGroupChat);
router.post('/leaveGroupChat', leaveGroupChat);
router.post('/updateNotify', updateOption);
router.post('/getAddMember', getFriends2AddMember);
router.post('/addMember', addMember);

export default router