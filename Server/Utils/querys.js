'use strict'

const db = require('./database_connect')

//returns all the players in the database
const getAll = (res) => {
    db.connect().query(
        'SELECT * FROM players',
        (err,results,fields) => {
            console.log(results)
            //console.log(fields)
            if (err == null){
                res.send(results);
            } else {
                console.log("Select error: "+ err)
            }
        }
    )
}
//Creates a new player in the database
const createNewPlayer = (data,res) => {
    db.connect().query(
        'SELECT name FROM players WHERE `name` = ?',
        data,
        (err,results,fields)=>{
            if (results.length === 0){
                db.connect().execute(
                    'INSERT INTO players (name, points) VALUES (?,20);',
                    data,
                    (err,results,fields) => {
                        console.log(results)
                        let responseData = {
                            response: "Created player with name: " + data[0], 
                            playerCreated: true
                        }
                        res.send(responseData)
                    }
                )
            } else {
                console.log("Player aleady exists")
                let responseData = {
                    response: "player with name: "+data[0]+ " already exists!",
                    playerCreated: false
                }
                res.send(responseData)
            }  
        }
    )
}
//Just returns player name and points
const getPlayerPoints = (data,res) => { 
    db.connect().query(
        'SELECT name,points FROM players WHERE `name` = ?',
        data,
        (err,results,fields)=>{
            if (err == null){
                let responseData = {
                    name: results[0].name,
                    points: results[0].points,
                }
                res.send(responseData)
            } else {
                console.log("error")
                let error = {error: "404"}
                res.send(error)
            }
        }
    )
}

//Helper function to handle updates when adding points to players
const exec = (points,data,res,clickAmount)=> {
    const selectQ = 'SELECT name,points FROM players WHERE `name` = ?'
    const addPointsQ = 'UPDATE players SET points = points + '+ points +' WHERE `name` = ?'
    db.connect().execute(
        addPointsQ,
        data,
        (err,results,fields)=>{
            if (err==null){
                db.connect().query(
                    selectQ,
                    data,
                    (err,results,fields)=>{
                        //res.send(results)
                        if (results[0].points <= 0){
                            let responseData= {
                                response: "No more points",
                                pointsEnded: true
                            }
                            res.send(responseData)
                        } else {
                            let toNextReward = 10-(clickAmount%10)
                            let responseData = {
                                name: results[0].name,
                                points: results[0].points,
                                clicksToNextReward: toNextReward,
                                pointsEnded: false
                            }
                            res.send(responseData)
                        }
                    }
                )
            }
        }
    )
}

//Does point handling.
const pointHandler = (data,res,clickAmount) =>{
    if (clickAmount%500 === 0){
        console.log("500th")
        exec(250,data,res,clickAmount)
    } else if(clickAmount%100 === 0){
        console.log("100th")
        exec(40,data,res,clickAmount)
    } else if(clickAmount%10 === 0){
        console.log("10th")
        exec(5,data,res,clickAmount)
    } else {
        console.log("nothing")
        exec(-1,data,res,clickAmount)
    }
}

const resetPlayer = (data,res) => {
  const selectQ = 'SELECT name,points FROM players WHERE `name` = ?'
  db.connect().query(
      selectQ,
      data,
      (err,results,fields)=>{
        if (err==null){
          db.connect().execute(
              'UPDATE players SET points = 20 WHERE `name` = ?',
              data,
              (err,fields)=>{
                let responseData = {
                  response: "Reset Points to 20",
                  name: results[0].name,
                  points: 20,
                }
                res.send(responseData)
              }
          )
        }
      }
  )
}
        
    




module.exports = {
    getAll: getAll,
    createNewPlayer: createNewPlayer,
    getPlayerPoints: getPlayerPoints,
    pointHandler: pointHandler,
    resetPlayer: resetPlayer,
}