const { PrismaClient } =require('@prisma/client');
const prisma = new PrismaClient()
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const signJwt = (id) => {
    return jwt.sign({ id }, 'secret');
};
exports.login = async (req, res) => {

    const { email, password } = req.body;
    try {
        const user = await prisma.user.findUnique({
            where: {
                email
            }
        });
        if (!user) {
            console.log("user check");
            return res.status(404).json({ message: 'User Not found.',token:"" });
        }
        const passwordIsValid = bcrypt.compareSync(password, user.password);
        if (!passwordIsValid) {
            console.log("password check");
            return res.status(401).json({
                accessToken: null,
                message: 'Invalid Password!'
            });
        }
        const token = signJwt(user.id);
        console.log(token);
        res.cookie('jwt',token );
        console.log(user.email);
        res.status(200).json({ message: 'User was logged in successfully!',email:user.email,token });
    } catch (err) {
        console.log(err.message);
        res.status(500).json({ message: err.message,token:"" });
    }
};
exports.register = async (req, res) => {
    const {username,  email, password } = req.body;
    console.log(req.body);
    // console.log(req.body);
    try {
        const user = await prisma.user.create({
            data: {
                name: username,
                email,
                password: bcrypt.hashSync(password, 8)
            }
        });
        const token = signJwt(user.id);
        res.cookie('jwt',token );
        console.log(token);
        res.status(201).json({ message: 'User was registered successfully!' ,email,token});
    } catch (err) {
        console.log(err.message);
        res.status(400).json({ message: err.message,token:"" });
    }
};
exports.me = async (req, res) => {
    try {
        const token = req.body.token;
        console.log(token);
        if (!token) {
            console.log("token check");
            return res.status(401).json({ message: 'No token provided!' });
        }
        const decoded = jwt.verify(token, 'secret');
        const user = await prisma.user.findUnique({
            where: {
                id: decoded.id
            },
            select: {
                id: true,
                name: true,
                email: true,
                phone: true,
            }
        });
        if (!user) {
            console.log("error");
            return res.status(404).json({ message: 'User Not found.' });
        }
        console.log(user);
        res.status(200).json(user);
    } catch (err) {
        console.log(err.message);
        
        res.status(500).json({ message: err.message });
    }
};
