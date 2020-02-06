'use strict'

const db = require('./database_connect')


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



module.exports = {
    getAll: getAll,
}