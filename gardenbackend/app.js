const express = require('express');
const app = express();
const cors = require('cors');
const userRouter = require('./routers/userRouter');
const flowersRouter = require('./routers/flowersRouter');
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.all('*', (req, res,next) => {
    console.log(req.body);    
    next();
});

app.use('/api/users', userRouter);
app.use('/api/flowers', flowersRouter);


module.exports = app;