const router = require('express').Router();
const flowersController = require('../controllers/flowersController');
router.route('/').get(flowersController.getflowers);
router.route('/add').post(flowersController.addFlower);
router.route("/:id").get(flowersController.getFlowerById);
module.exports = router;