# ClickerGame

## What is it?

ClickerGame is a simple multiplayer clicking game where players gain different amounts of score according to clicks. Every 10th click will give the clicker 5 points, 100th click will give 40 points and 500th click will give 250 points. All players increase the same click amount.

## How to Play

Download [app-debug.apk](https://github.com/arttujo/ClickerGame/raw/master/app-debug.apk) from this repo and just install. . Right now there is an instance of the game server running on my personal home server so if the app doesn't work from the get-go there is an issue with my server or it has crashed. Other way to run the app is to download this repo and use Android Studio to run the android project. 


## How it works
On application launch the app checks if the client has an UUID. If not the app creates on for the user. This UUID is used to identify the user on the server. Uninstalling and installing the app will result in a new UUID. The app uses shared preferences to store the UUID. Shared preferences are also used to save the score the user has. 

When playing the app makes post requests to the server to get the score and update it to the user. App notifies the user everytime he/she earns points or runs out of points. 

The server contains a variable that basically follows the amount of certain requests the server has received. Each one of this requests increases the variable by 1. If the variable produces a remainder of 0 with either 10, 100 or 500 it will give the user score. If the variable doesn't produce a remainder of 0 it will reduce 1 point from the player. 


## How to host by yourself

**Step 1:**
```
Create .env file into Server directory with the following parameters:
- DB_HOST = "hostaddress"
- DB_USER = "database user with permissions for the games database"
- DB_NAME = "database name"
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
    "name": username sent with the post request,
    "points": amount of point the user has",
    "clicksToNextReward": amount of clicks to next reward,
    "pointsEnded": boolen. either true or false depending if player has reached 0 points or not,
    "pointsEarned": the amount of points the user has earned
}
```
## Other
**Tools Used:**

- Android Studio for app development
- Visual Studio Code for Node.js server development
- Ubuntu server to host the server
- Postman to make quick request tests
- PhpMyAdmin for Database creation


**Known Issues:**

- ~~Database might get stuck after too many connections.~~ Should be fixed
- ~~Server crashes when use spams too fast.~~ Should be fixed
