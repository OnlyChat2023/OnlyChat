import express from 'express';
import authController from '../controllers/authController.js';

const router = express.Router();

/* 
    AUTH ROUTES
    1. login
    2. register
    3. forgot password
    4. reset password
*/

/// > LOGIN
router.post('/login', authController.login);

//// > REGISTER
router.post('/register/validate', authController.validateRegister);
router.post('/register', authController.register);

//// > FORGOT PASSWORD
router.post('/forgot', authController.forgot);

//// > RESET PASSWORD
router.patch('/reset', authController.reset);

export default router;