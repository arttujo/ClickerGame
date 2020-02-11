'use strict'
require('dotenv').config()
const mysql = require('mysql2')


const connect = () => {
    const connection = mysql.createPool({
        host: process.env.DB_HOST,
        user: process.env.DB_USER,
        database: process.env.DB_NAME,
        password: process.env.DB_PASS,
        waitForConnections: true,
        connectionLimit: 50,
        queueLimit: 0
    })
    return connection
}

module.exports = {
    connect: connect,
}