package com.example.projectapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    //initializing tag for logger
    private var tag:String = "LocationLogger"

    //initializing views
    private lateinit var nameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var loginButton: Button
    private var preference: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(tag, "onCreate: Starting Login Screen")

        //assigning views with id's
        nameEditText = findViewById(R.id.nameEditText)
        mobileEditText = findViewById(R.id.mobileEditText)
        loginButton = findViewById(R.id.loginButton)

        //initializing shared preference
        Log.d(tag, "onCreate: Setting up shared preference")
        preference = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)

        //we are setting up on click listener
        loginButton.setOnClickListener {
            Log.d(tag, "onCreate: Login Button Clicked")
            //storing values in shared preference
            Log.d(tag, "onCreate: Initializing shared preference editor")
            val editor = preference!!.edit()
            editor.putString("nameKey", nameEditText.text.toString())
            editor.putString("mobileKey", mobileEditText.text.toString())
            editor.putBoolean("bool", true)
            editor.apply()
            Log.d(tag, "onCreate: Values sent to shared preference")

            //starting activity
            Log.d(tag, "onCreate: Starting Main Activity")
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "login success", Toast.LENGTH_LONG).show()
        }
    }
}