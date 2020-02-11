package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import retrofit2.http.HTTP
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {

    private var uniqueID:String?= null
    private var PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createUUID(this)

        playButton.setOnClickListener {
            Log.d("DBG",getUUID(this))


            doAsync {
                var json: JSONObject
                //TODO GET REQUEST FOR POINT HANDLING HERE
                Fuel.post("http://foxer153.asuscomm.com:3000/removePoint")
                    .jsonBody("{\"name\":\"test1\"}")
                    .also { Log.d("DBG", it.toString()) }
                    .response { result ->
                        Log.d("DBG",result.toString())
                        val (bytes,error) = result
                        if (bytes!=null){
                            json = JSONObject(String(bytes))
                            Log.d("DBG",json.toString())
                            Log.d("DBG",json.getBoolean("pointsEnded").toString())
                            if (json.getBoolean("pointsEnded")){
                                //TODO tell user that game will start over for him
                                Fuel.post("http://foxer153.asuscomm.com:3000/reset")
                                    .jsonBody("{\"name\":\"test1\"}")
                                    .also { Log.d("DBG",it.toString()) }
                                    .response { result ->
                                        Log.d("DBG","user reset")
                                        uiThread {
                                            buildAlert(this@MainActivity)
                                            pointLabel.text = "20"
                                        }
                                    }
                            }
                            Log.d("DBG",json.getString("name"))
                            Log.d("DBG",json.getInt("points").toString())


                            uiThread {
                                //TODO CHANGE SCORE LABEL AND CLICKS TO LABEL HERE
                                pointLabel.text = json.getString("points")
                                pointsToLabel.text = json.getString("clicksToNextReward")
                            }
                        }
                    }

            }
        }

    }

    //Alerts the user that he has ran out of points and notifies that the game will start over for him
    private fun buildAlert(ctx:Context){
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(R.string.game_over)
        builder.setMessage(R.string.no_more_points)
        builder.setPositiveButton("Ok"){
            dialog, which ->
            Log.d("DBG","yes clicked on alert")
        }
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    //Returns UUID as a string
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
