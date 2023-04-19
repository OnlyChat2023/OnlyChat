import express from 'express';
import rateLimit from 'express-rate-limit';
import mongoSanitize from 'express-mongo-sanitize';
import xss from 'xss-clean';
import hpp from 'hpp';
import cors from 'cors';
import userProfileRouter from "./routes/userProfileRoutes.js"
import globalErrorhandler from "./controllers/ErrorController.js"
import groupChatRouter from './routes/groupChatRoutes.js';
import globalChatRouter from './routes/globalChatRoutes.js';
import directMessageRouter from './routes/directMessageRoutes.js'
import metaDataRouter from './routes/metaDataRoutes.js'

import messageRoutes from "./routes/messageRoutes.js"

import authRouter from './routes/authRoutes.js';

const limiter = rateLimit({
  // limiter is now become a middleware function
  max: 1000,
  windowMs: 60 * 60 * 1000,
  message: 'Too many requests from this IP, please try this again in an hour!',
}); // define how many requests per IP we are going to allow in a certain of time

const app = express();
// app.use(limiter);
app.use(cors());
app.use(mongoSanitize());
app.use(xss());
app.use(hpp());
  
app.use("/assets",express.static("assets"));

app.use("/assets", express.static("assets"));

app.use(express.json({ limit: '10mb' }));

app.use('/api/onlychat/v1/auth', authRouter);
app.use('/api/onlychat/v1/user', userProfileRouter); // mounting new router on route (URL)
app.use('/api/onlychat/v1/groupChat', groupChatRouter);
app.use('/api/onlychat/v1/globalChat', globalChatRouter);
app.use('/api/onlychat/v1/directMessage', directMessageRouter);
app.use('/api/onlychat/v1/metadata', metaDataRouter);

app.use(globalErrorhandler);

export default app;