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
                        }
                        res.send(responseData)
                    }
                )
            } else {
                console.log("Player aleady exists")
                let responseData = {
                    response: "player with name: "+data[0]+ " already exists!"
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
                res.send(results)
            } else {
                console.log("error")
                let error = {error: "404"}
                res.send(error)
            }
        }
    )
}
// UPDATE `players` SET `points`= 2 WHERE `name` = "test5"
//Removes one point from the person and returns a response with current points and name
//Also checks if the player runs out of points and sends a response telling that
const removePoint = (data,res) =>{
    db.connect().execute(
        'UPDATE players SET points = points - 1 WHERE `name` = ?',
        data,
        (err,results,fields)=>{
            if(err == null){
                db.connect().query(
                    'SELECT name,points FROM players WHERE `name` = ?',
                    data,
                    (err,results,fields)=>{
                        if (results[0].points === 0){
                            console.log("No more points")
                            let responseData= {
                                response: "No more points",
                                pointsEnded: true
                            }
                            res.send(responseData)
                        }
                        res.send(results)
                    }
                )
            }
        }
    )
}


const pointHandler = (data,res,clickAmount) =>{
    const selectQ = 'SELECT name,points FROM players WHERE `name` = ?'
    const rmOnePointQ = 'UPDATE players SET points = points - 1 WHERE `name` = ?'
    const add5PointsQ = 'UPDATE players SET points = points + '+ 5 +' WHERE `name` = ?'
    const add40PointsQ = 'UPDATE players SET points = points + '+ 40 +' WHERE `name` = ?'
    const add250PointsQ = 'UPDATE players SET points = points + '+ 250 +' WHERE `name` = ?'

    if (clickAmount%500 == 0){
        console.log("500th")
        db.connect().execute(
            add250PointsQ,
            data,
            (err,results,fields)=>{
                if (err==null){
                    db.connect().query(
                        selectQ,
                        data,
                        (err,results,fields)=>{
                            res.send(results)
                        }
                    )
                }
            }
        )

       
    } else if(clickAmount%100 == 0){
        console.log("100th")
        db.connect().execute(
            add40PointsQ,
            data,
            (err,results,fields)=>{
                if (err==null){
                    db.connect().query(
                        selectQ,
                        data,
                        (err,results,fields)=>{
                            res.send(results)
                        }
                    )
                }
            }
        )

    } else if(clickAmount%10 == 0){
        console.log("10th")
        db.connect().execute(
            add5PointsQ,
            data,
            (err,results,fields)=>{
                if (err==null){
                    db.connect().query(
                        selectQ,
                        data,
                        (err,results,fields)=>{
                            res.send(results)
                        }
                    )
                }
            }
        )

    } else {
        console.log("nothing")
        db.connect().execute(
            rmOnePointQ,
            data,
            (err,results,fields)=>{
                if (err==null){
                    db.connect().query(
                        selectQ,
                        data,
                        (err,results,fields)=>{
                            if (results[0].points === 0){
                                console.log("No more points")
                                let responseData= {
                                    response: "No more points",
                                    pointsEnded: true
                                }
                                res.send(responseData)
                            } else {
                                res.send(results)
                            }
                        }
                    )
                }
            }
        )
    }
}


        
    




module.exports = {
    getAll: getAll,
    createNewPlayer: createNewPlayer,
    getPlayerPoints: getPlayerPoints,
    removePoint: removePoint,
    pointHandler: pointHandler,
}