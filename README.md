# ClickerGame




## How to host by yourself

**Step 1:**
```
Create .env file into Server directory with the following parameters:
- DB_HOST = "hostaddress"
- DB_USER = "database user with permissions for the games database"
- DB_NAME = "name of your database"
- DB_PASS = "password for the user who uses the database"
```
```
For Example:
 DB_HOST = localhost (or url to your remote server)
 DB_USER = game_db_user
 DB_NAME = gamedb
 DB_pass = 1234
```

**Step 2:**

A simple SQL Database is need to for the Node.js server to work. My database only contains one table with 2 fields: player & points.
Example of a database creation script:
```
CREATE DATABASE IF NOT EXISTS `Clicker_Game` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `Clicker_Game`;
CREATE TABLE `players` (
  `name` varchar(64) NOT NULL,
  `points` int(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO `players` (`name`, `points`) VALUES
('testuser1', 20),
('testuser2', 20),
('testuser3', 20),
('testuser4', 20),
('testuser5', 20);
```

**Step 3:**

When you have your Database and Node server up and running you can try making request to the server. I use [Postman](https://www.postman.com/). Example Post request: 
```
"yourseverurl":3000/removePoint
Send a json with body:
{
"name": "testuser1"
}
```
If the request was successfull you should get a response: 
```
{
    "name": "username sent with the post request",
    "points": "amount of point the user has",
    "clicksToNextReward": "amount of clicks to next reward",
    "pointsEnded": boolen. either true or false depending if player has reached 0 points or not
}
```


**Known Issues:**

- Database might get stuck after too many connections.
- Server crashes when use spams too fast 
