package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.style.TtsSpan
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var postData:JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton.setOnClickListener {
            doAsync {

                try {
                    //var deviceName = Settings.Global.getString(contentResolver,Settings.Global.DEVICE_NAME)
                    //Log.d("DBG",deviceName)
                    //var deviceBtName = BluetoothAdapter.getDefaultAdapter()
                    //Log.d("DBG",deviceBtName.name)
                    var manager:TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    var imei = manager.imei
                    Log.d("DBG",imei)

                    postData.put("name", "test")
                } catch (e:Exception){

                }
            }
        }

    }
}
