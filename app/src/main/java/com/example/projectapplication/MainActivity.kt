package com.example.projectapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {
    //initializing tag for logger
    private var tag:String = "LocationLogger"

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
        Log.d(tag, "onCreate: Starting Main Screen")

        //setting up client
        Log.d(tag, "onCreate: Setting up fused location client")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //assigning views with id's
        Log.d(tag, "onCreate: Defining views in appropriate views")
        nameTextView = findViewById(R.id.nameTextView)
        mobileTextView = findViewById(R.id.mobileTextView)
        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)

        //shared preference reference
        Log.d(tag, "onCreate: Getting data from shared preference")
        val sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
        val nameSp = sharedPreferences.getString("nameKey", "")
        val mobileSp = sharedPreferences.getString("mobileKey", "")

        //calling function for getting current location
        Log.d(tag, "onCreate: Requesting current location")
        getCurrentLocation()
        Log.d(tag, "onCreate: Setting text fetched from shared preference")
        nameTextView.text = nameSp
        mobileTextView.text = mobileSp
    }

    //it will provide application by going to login screen
    override fun onBackPressed() {
        Log.d(tag, "onCreate: Starting Main Activity")
        startActivity(Intent(applicationContext, MainActivity::class.java))
        super.onBackPressed()
    }

    //that's the method where we get our current location
    private fun getCurrentLocation() {
        Log.d(tag, "getCurrentLocation: Method for getting current location started")
        if (checkPermission()) {
            Log.d(tag, "getCurrentLocation: permission granted")
            if (isLocationEnable()) {
                Log.d(tag, "getCurrentLocation: location is enabled")
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location = task.result
                    Toast.makeText(this, "location received", Toast.LENGTH_SHORT).show()
                    latitudeTextView.text = location.latitude.toString()
                    longitudeTextView.text = location.longitude.toString()
                }
            } else {
                Log.d(tag, "getCurrentLocation: permission denied")
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            Log.d(tag, "getCurrentLocation: Requesting permissions")
            requestPermissions()
        }
    }

    // in this method we check if the location is enabled or not
    private fun isLocationEnable(): Boolean {
        Log.d(tag, "isLocationEnable: checking location is enabled or not")
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //in this method we are going to request for permissions
    private fun requestPermissions() {
        Log.d(tag, "requestPermissions: Requesting permissions")
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
        Log.d(tag, "checkPermission: Checking Permissions")
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
                Log.d(tag, "onRequestPermissionsResult: Getting current location")
                getCurrentLocation()
            } else {
                Log.d(tag, "onRequestPermissionsResult: Permission not granted")
                Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}