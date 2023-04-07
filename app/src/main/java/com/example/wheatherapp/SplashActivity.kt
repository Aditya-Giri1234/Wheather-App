package com.example.wheatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.wheatherapp.databinding.ActivitySplashBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class SplashActivity : AppCompatActivity() {
    lateinit var mfusedLocation:FusedLocationProviderClient
    lateinit var binding:ActivitySplashBinding
    private var mRequestCode=1010;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mfusedLocation=LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

    }

    // 1.location permisision -> deny
    // 2.loactiob permissiion denied through settings
    //3 . gps off
    //4 permission lena

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(CheckPermission()){
            if(locationEnabled()){
               mfusedLocation.lastLocation.addOnCompleteListener { task->
                   var location:Location?=task.result;
                   if(location==null){
                       NewLocation();

                   }
                   else{
                       Handler(Looper.getMainLooper()).postDelayed({
                                 val intent =Intent(this,MainActivity::class.java)
                           intent.putExtra("lat",location.latitude.toString())
                           intent.putExtra("long",location.longitude.toString())
                           startActivity(intent)
                           finish()
                       },2000);
                   }
                }
            }
            else{
                Toast.makeText(this,"Please turn on gps",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {
        var locationReequest= com.google.android.gms.location.LocationRequest()
        locationReequest.priority=com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationReequest.interval=0;
        locationReequest.fastestInterval=0;
        locationReequest.numUpdates=1
        mfusedLocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedLocation.requestLocationUpdates(locationReequest,locationCallback, Looper.myLooper())



    }
    val locationCallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastlocation: Location? = p0.lastLocation
        }

    }

    private fun locationEnabled(): Boolean {
       var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
       ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),mRequestCode)
    }

    private fun CheckPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==mRequestCode){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 getLastLocation()
            }
        }

    }

}