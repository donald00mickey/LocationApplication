package com.example.projectapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    //initializing views
    private lateinit var nameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var loginButton: Button
    var sharedpreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //assigning views with id's
        nameEditText = findViewById(R.id.nameEditText)
        mobileEditText = findViewById(R.id.mobileEditText)
        loginButton = findViewById(R.id.loginButton)

        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        //we are setting up on click listener
        loginButton.setOnClickListener {
            //declaring intent
            val intent = Intent(this, MainActivity::class.java)
            //sending values form one activity to another

            val editor = sharedpreferences!!.edit()
            editor.putString("nameKey", nameEditText.text.toString())
            editor.putString("mobileKey", mobileEditText.text.toString())
            editor.putBoolean("bool", true)
            editor.apply()

            intent.putExtra("nameKey", nameEditText.text.toString())
            intent.putExtra("mobileKey", mobileEditText.text.toString())
            intent.putExtra("bool", true)
            //starting activity
            startActivity(intent)
            Toast.makeText(this, "login success", Toast.LENGTH_LONG).show()
        }
    }
}