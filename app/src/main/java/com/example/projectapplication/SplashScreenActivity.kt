package com.example.projectapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sh = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
        val a = sh.getBoolean("bool", true)
        !a

        if (a){
            Handler().postDelayed({ // This method will be executed once the timer is over
                val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }, 5000)
        }else{

            Handler().postDelayed({ // This method will be executed once the timer is over
                val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }, 5000)
        }


    }
}