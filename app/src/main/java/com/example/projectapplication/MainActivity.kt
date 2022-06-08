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

        //share preference reference
        val sh = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
        val s1 = sh.getString("nameKey", "")
        val s2 = sh.getString("mobileKey", "")

        if (nameTextView.text.isBlank() && mobileTextView.text.isBlank()){
            startActivity(Intent(this, LoginActivity::class.java))
        }else{
            Toast.makeText(this, "location recieved", Toast.LENGTH_SHORT).show()
        }

        //calling function for getting current location
        getCurrentLocation()
        nameTextView.text = s1
        mobileTextView.text = s2
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