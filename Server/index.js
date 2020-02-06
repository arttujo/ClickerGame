const express = require('express')
require('dotenv').config()
const queries = require('./Utils/querys')
const db = require('./Utils/database_connect')
const bodyParser = require('body-parser')


const app = express()
/*
app.use(session({
    secret: 'do not touch',
    resave: false,
    saveUnitialized: false,
    cookie: {
        httpOnly: false,
        maxAge: null,
    },
}))
*/

app.use(bodyParser.json())
app.use(express.static('public'))
app.use('/modules', express.static('node_modules'));
app.get('/all',(req,res) =>{
    queries.getAll(res)
})

app.listen(3000, ()=> {console.log("App is ready and listening to port: 3000!")})