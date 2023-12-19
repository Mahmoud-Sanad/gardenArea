
const { PrismaClient } =require('@prisma/client');
const prisma = new PrismaClient()
const jwt = require('jsonwebtoken');

exports.getflowers = async (req, res) => {
    try {
        const flowers = await prisma.product.findMany();
        res.status(200).json(flowers);
    } catch (err) {
        console.log(err.message);
        res.status(500).json({ message: err.message });
    }
}
exports.getFlowerById = async (req,res)=>{
    const {id} = req.params
    try {
        const flower = await prisma.product.findUnique({
            where:{
                id:parseInt(id)
            }
        });
        res.status(200).json(flower);
        
    } catch (error) {
        console.log(error.message);
        res.status(500).json({message:error.message});
    }
}
exports.addFlower = async (req, res) => {
    var { name, price, description, image } = req.body;
    price = parseInt(price);
    try {
        const flower = await prisma.product.create({
            data: {
                name,
                price,

               
                image
            }
        });
        res.status(201).json({ message: 'Flower was added successfully!' });
    } catch (err) {
        console.log(err.message);
        res.status(400).json({ message: err.message });
    }
}
exports.addToCart = async (req, res) => {

    var { quantity,id,token } = req.body;
    quantity = +quantity;
    id=+id;
    try {
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
                cart:{
                    select:{
                        id:true
                    }
                }
            }
        });
        if (!user) {
            console.log("error");
            return res.status(404).json({ message: 'User Not found.' });
        }
        console.log(user);
        var cartItem = await prisma.cartItem.findFirst({
            where:{
                productId:id,
                cartId:user.cart.id,
            }
        });
        console.log(cartItem);
        if(cartItem){
             cartItem = await prisma.cartItem.update({
                where:{
                    id:cartItem.id,
                },
                data:{
                    quantity:{
                        increment:quantity,
                    }
                }
            });
            return res.status(201).json({ message: 'Cart was updated successfully!' });
        }
        else {
             cartItem = await prisma.cartItem.create({
                data:{
                    quantity,
                    productId:id,
                    cartId:user.cart[0].id,
                }
            });
            return res.status(201).json({ message: 'Cart was added successfully!' });
        }
    } catch (err) {
        console.log(err.message);
        res.status(400).json({ message: err.message });
    }
}
exports.cart = async (req, res) => {
    try {
        const token = req.body.token;
        console.log(token);
        if (!token) {
            console.log("token check");
            return res.status(401).json({ message: 'No token provided!' });
        }
        const decoded = jwt.verify(token, 'secret');
       const cart = await prisma.cart.findFirst({
            where:{
                userId:+decoded.id,
            },
            
            select:{
                cartItem:{
                    select:{
                        product:true,
                        quantity:true,
                        cartId:false,
                        createdAt:false,
                        productId:false,
                        updatedAt:false,
                    }
                }

            }
        });
        if (!cart) {
            console.log("error");
            return res.status(404).json({ message: 'User Not found.' });
        }
        console.log(cart.cartItem);
        res.status(200).json(cart.cartItem);
    } catch (err) {
        console.log(err.message);
        res.status(400).json({ message: err.message });
    }
}
exports.emptyCart = async (req, res) => {
    try {
        const token = req.body.token;
        console.log(token);
        if (!token) {
            console.log("token check");
            return res.status(401).json({ message: 'No token provided!' });
        }
        const decoded = jwt.verify(token, 'secret');
       const cart = await prisma.cart.findFirst({
            where:{
                userId:+decoded.id,
            },
            
            select:{
                id:true,
                cartItem:{
                    select:{
                        product:true,
                        quantity:true,
                        cartId:false,
                        createdAt:false,
                        productId:false,
                        updatedAt:false,
                    }
                }

            }
        });
        if (!cart) {
            console.log("error");
            return res.status(404).json({ message: 'User Not found.' });
        }
        else {
            await prisma.cartItem.deleteMany({
                where:{
                    cartId:cart.id
                }
            })
        }
        res.status(200).json({message:"Purchased Succesfuly"});
    } catch (err) {
        console.log(err.message);
        res.status(400).json({ message: err.message });
    }
}