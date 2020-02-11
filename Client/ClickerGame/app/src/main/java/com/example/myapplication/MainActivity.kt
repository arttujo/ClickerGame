//Created by Arttu Jokinen

package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private var url = "http://foxer153.asuscomm.com:3000"
    private var uniqueID:String?= null
    private var PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    private var PREF_POINTS = "PREF_POINTS"

    override fun onCreate(savedInstanceState: Bundle?) {
        createUUID(this)
        newUser(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //This listener does all the handling related to the points of the user.
        playButton.setOnClickListener {
            doAsync {
                var json: JSONObject
                Fuel.post("$url/removePoint")
                    .jsonBody("{\"name\":\"${getUUID(this@MainActivity)}\"}")
                    .also { Log.d("DBG", it.toString()) }
                    .response { result ->
                        val (bytes,error) = result
                        if (bytes!=null){
                            json = JSONObject(String(bytes))
                            Log.d("DBG",json.toString())
                            if (json.getBoolean("pointsEnded")){
                                //If pointsEnded = true, makes a new request to reset the players points
                                Fuel.post("$url/reset")
                                    .jsonBody("{\"name\":\"${getUUID(this@MainActivity)}\"}")
                                    .also { Log.d("DBG",it.toString()) }
                                    .response { result ->
                                        Log.d("DBG","user reset")
                                        uiThread {
                                            buildAlert(this@MainActivity)
                                            //Short cutting here to just set the value instead of handling the result json
                                            pointLabel.text = "20"
                                        }
                                    }
                            }
                            Log.d("DBG",json.getString("name"))
                            Log.d("DBG",json.getInt("points").toString())

                            uiThread {
                                val curPoints= Integer.parseInt(pointLabel.text.toString())
                                if (curPoints<json.getInt("points")){
                                    prizeAlert(this@MainActivity,json.getString("pointsEarned"))
                                }
                                pointLabel.text = json.getString("points")
                                pointsToLabel.text = json.getString("clicksToNextReward")
                            }
                        }
                    }
            }
        }
    }

    //Sends a post request to the server and from the result saves them to shared preferences
    //Also used to display users score when the application is launched.
    private fun setPoints(ctx:Context, uuid: String){
        val sharedPrefs = ctx.getSharedPreferences(PREF_POINTS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        doAsync {
            Fuel.post("$url/player")
                .jsonBody("{\"name\":\"${uuid}\"}")
                .also { Log.d("DBG",it.toString()) }
                .response { result ->
                    val (bytes,error) = result
                    if (bytes!=null){
                        val response = JSONObject(String(bytes))
                        val points = response.getInt("points")
                        editor.putInt(PREF_POINTS,points)
                        editor.apply()
                        uiThread {
                            val pointsfrompref = sharedPrefs.getInt(PREF_POINTS,0)
                            pointLabel.text = pointsfrompref.toString()
                        }
                    }
                }
        }
    }

    //Creates new user if the user doesn't exist. Notifies user with a Snackbar when user is created.
    //Doesn't do anything if the user already has an "account"
    private fun newUser(ctx:Context){
        doAsync {
            Fuel.post("$url/newPlayer")
                .jsonBody("{\"name\":\"${getUUID(ctx)}\"}")
                .also { Log.d("DBG",it.toString()) }
                .response { result ->
                    val (bytes,error) = result
                    if (bytes!=null){
                        val response = JSONObject(String(bytes))
                        Log.d("DBG",response.toString())
                        if (response.getBoolean("playerCreated")){
                            uiThread {
                                Snackbar.make(coordinator,R.string.player_created,Snackbar.LENGTH_LONG)
                                    .setAction("Ok"){}
                                    .show()
                                setPoints(ctx,getUUID(ctx))
                            }
                        } else if (!response.getBoolean("playerCreated")){
                            setPoints(ctx,getUUID(ctx))
                        }
                    }
                }
        }
    }
    //Alerts the user that he has won a certain amount of points when the user wins points
    private fun prizeAlert(ctx:Context, points:String){
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(R.string.congrat)
        builder.setMessage("${ctx.getString(R.string.won_points_start)} $points ${ctx.getString(R.string.won_points_end)}")
        builder.setPositiveButton("Ok"){dialog, which ->  Log.d("DBG", "clicked on win alert")}
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()
    }
    //Alerts the user that he has ran out of points and notifies that the game will start over
    private fun buildAlert(ctx:Context){
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(R.string.game_over)
        builder.setMessage(R.string.no_more_points )
        builder.setPositiveButton("Ok"){ dialog, which -> Log.d("DBG","clicked on alert")}
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    //Returns UUID as string
    private fun getUUID(ctx:Context):String{
        val sharedPref = ctx.getSharedPreferences(PREF_UNIQUE_ID,Context.MODE_PRIVATE)
        val uuid = sharedPref.getString(PREF_UNIQUE_ID,uniqueID)
        return uuid!!
    }
    //Creates an UUID to identify the client on the server. UUID is store in the SQL database name field
    private fun createUUID(ctx:Context){
        if(uniqueID == null){
            val sharedPrefs: SharedPreferences = ctx.getSharedPreferences(PREF_UNIQUE_ID,Context.MODE_PRIVATE)
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID,null)
            if (uniqueID==null){
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID,uniqueID)
                editor.apply()
            }
        }
    }
}