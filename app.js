const Joi = require('joi');//Joi is a class here,that's why uppercase
const express = require('express');
const app = express();//express Object

//need this to use request body :3
app.use(express.json());


const mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/player-test')
    .then(() => console.log('connected to database'))
    .catch(err => console.error(err.message));

const playerSchema = new mongoose.Schema({
    name: String,
    position: String,
    jersey: Number,
    date: {
        type: Date, default: Date.now
    },
});

//now this will give us a class
const Player = mongoose.model('Player', playerSchema);


//applying for courses array

app.get('/api/courses/:id',(req,res)=>{
    const course = courses.find(c => c.id === parseInt(req.params.id));
    if(!course)return res.status(404).send('not found')
    res.send(course);
});//url + callback function


app.post('/save', (req,res)=>{
    
    console.log(req.body);
    const { error } = validatePlayerName(req.body);//object distractor
    //input validation
    if(error){
        //bad request 
        console.log(error);
        res.status(400).send(error.details[0].message);
        return;
    }
    const player = createPlayer(req.body.name,req.body.position,req.body.jersey);
    res.status(200).send(JSON.stringify(player));
});//url + callback function



function validatePlayerName(player){
    //package for input validation - joi
    const schema = {
        name: Joi.string().min(2).required(),
        position: Joi.string().min(2).required(),
        jersey: Joi.string().max(2).required(),
    }

    return Joi.validate(player, schema);
}


//using environment variable
const port = process.env.PORT || 3000
app.listen(port,()=>{
    console.log(`Listening on port ${port}`);
});


async function createPlayer(n,p,j){
    const player = new Player({
        name: n,
        position: p,
        jersey: j
    })

    const result = await player.save();
    console.log(result);
    return player;
}

//createPlayer();

async function getPlayers(){
    //eq (equal) { $eq: -- }
    //ne (not equal) { $ne: -- }
    //gt (greater than) { $gt: -- }
    //gte (greater than equal) { $gte: -- }
    //lt { $lt: -- }
    //lte { $lte: -- }
    //in { $in: [--,--] }
    //nin(not in) { $nin: [--,--] }


    //logical operator
    //and 
    //or

    const pageNumber = 12
    const pageSize = 2

    const players = await Player
        .find({name: 'Asif Imtial'}) 
        //.find({price: {$gt 10}}, isPublished: true})
        //.skip((pageNumber - 1)*pageSize)//skipping all the docs 
        //in the previous (p-1) pages.
        .limit(pageSize)//only to show one page
        //.sort({ tags: 1 })//1 means ascending
        .select({ name: 1, position: 1 })
        .count();//returns the count of rows :P
    console.log(players);

    //using logical operators
    //.find()
    //.or( [{author: 'Adiba'}, {isPublished: true}] ) 
        
}

//getPlayers();


async function updatePlayer(id){
    //find course by id
    //set new parameters in the course
    //save the document

    console.log('Id ',id);
    const player = await Player.findById(id);
        if(!player){console.log('returning ',player); return;}

        player.position = 'Defence';
        player.name = 'Shahrar';

        const result = await player.save();
        console.log('Result ',result);

        //another approach:
        //course.set({
            //isPublished: true
            //author: 'new author'
        //})
        
}

//updatePlayer('5eeae79baf9a630b58a31ac0');

async function removeCourse(id){
    //const result = await Player.deleteOne({ _id:id });
    const player = await Player.findByIdAndRemove({ _id:id });
    console.log(player);
}

//removeCourse('5eeb30aa24f2191ec8d05550');