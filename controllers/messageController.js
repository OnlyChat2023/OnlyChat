import messageModel from "../models/globalChatModel.js"
import catchAsync from "../utils/catchAsync.js"

const getAll = catchAsync(async (req, res) => {
  const messages = await messageModel.find()
  console.log(messages)

  res.status(200).json({
    status: 'success',
    data: { messages }
  })
})

export { getAll }