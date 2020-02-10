package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import retrofit2.http.HTTP
import java.net.HttpURLConnection
import java.util.*


class MainActivity : AppCompatActivity() {

    private var uniqueID:String?= null
    private var PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    private var client = OkHttpClient()

    private lateinit var postData:JSONObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createUUID(this)

        playButton.setOnClickListener {
            Log.d("DBG",getUUID(this))

            doAsync {
                //TODO GET REQUEST FOR POINT HANDLING HERE
                uiThread {
                    //TODO CHANGE SCORE LABEL AND CLICKS TO LABEL HERE
                }
            }
        }

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
