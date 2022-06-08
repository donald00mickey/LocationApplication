package com.example.projectapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //calling shared preference
        val sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("bool", false)
        !isLoggedIn

        //checking if logged in or not
        if (isLoggedIn){
            Handler().postDelayed({ // This method will be executed once the timer is over
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }, 3000)
        }else{
            Handler().postDelayed({ // This method will be executed once the timer is over
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }, 3000)
        }


    }
}