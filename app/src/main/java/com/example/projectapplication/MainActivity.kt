package com.example.projectapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    //setting up a default variable
    private var loggedIn: Boolean = false

    //initializing location provider
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //initializing views
    private lateinit var nameTextView: TextView
    private lateinit var mobileTextView: TextView
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting up client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //assigning views with id's
        nameTextView = findViewById(R.id.nameTextView)
        mobileTextView = findViewById(R.id.mobileTextView)
        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)

        //fetching data from Login activity using intents
        val intent = intent //initialising getIntent() method
        //fetching sent values
        val bool: Boolean = intent.getBooleanExtra("bool", false)
        val name: String? = intent.getStringExtra("nameKey")
        val mobile: String? = intent.getStringExtra("mobileKey")

        //updating loggedIn boolean value
        loggedIn = bool
        //calling function for getting current location
        getCurrentLocation()

        //checking if the value is changed or not
        if (!loggedIn) {
            //here value of logged is still false that's why it's getting sent to login activity
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            //here value of boolean is changed that's why there is no need to go to another activity
            //because user is already logged in
            nameTextView.text = name
            mobileTextView.text = mobile
        }
    }

    //that's the method where we get our current location
    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location = task.result
                    Toast.makeText(this, "location received", Toast.LENGTH_SHORT).show()
                    latitudeTextView.text = location.latitude.toString()
                    longitudeTextView.text = location.longitude.toString()
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    // in this method we check if the location is enabled or not
    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //in this method we are going to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    //we are declaring a constant values for permission request access variable
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    //in this method we are checking for permissions which are required by our application
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    //in this method it is written what will happen if permissions are granted or denied
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

