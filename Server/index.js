const express = require('express')
require('dotenv').config()
const queries = require('./Utils/querys')
const db = require('./Utils/database_connect')
const bodyParser = require('body-parser')


const app = express()

//The click amount of players is tracked via a simple variable on the server. Will reset everytime when the server is restarted tho

let currClicks = 0

app.use(bodyParser.json())
//app.use(express.static('public'))
app.use('/modules', express.static('node_modules'));


app.get('/all',(req,res) =>{
    queries.getAll(res)
})

app.post('/removePoint',(req,res)=>{
    const data = [
        req.body.name,
    ]
    currClicks+=1
    queries.pointHandler(data,res,currClicks)
})

app.post('/newPlayer',(req,res) =>{
    const data = [
        req.body.name,
    ];
    queries.createNewPlayer(data,res)
})

app.post('/player',(req,res) => {
    //console.log(res)
    console.log(req)
    const data = [
        req.body.name,
    ];
    console.log(data)
    queries.getPlayerPoints(data,res)
})

app.post('/reset',(req,res)=>{
    const data = [
        req.body.name,
    ]
    queries.resetPlayer(data,res)
})


app.listen(3000, ()=> {console.log("App is ready and listening to port: 3000!")})