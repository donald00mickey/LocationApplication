package com.example.projectapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    //initializing tag for logger
    private var tag: String = "LocationLogger"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d(tag, "onCreate: Starting Splash Screen")

        //calling shared preference
        val sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
        Log.d(tag, "onCreate: Fetching boolean from Shared Preference")
        val isLoggedIn = sharedPreferences.getBoolean("bool", false)
        !isLoggedIn

        //checking if logged in or not
        if (isLoggedIn) {
            Log.d(tag, "onCreate: Checking if value of logged in is true")
            Handler().postDelayed({ // This method will be executed once the timer is over
                Log.d(tag, "onCreate: Starting Main Activity")
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 3000)
        } else {
            Log.d(tag, "onCreate: Checking if value of logged in is false")
            Handler().postDelayed({ // This method will be executed once the timer is over
                Log.d(tag, "onCreate: Starting Login Activity")
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }, 3000)
        }


    }
}