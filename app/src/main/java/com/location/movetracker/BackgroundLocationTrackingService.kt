package com.location.movetracker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import com.iammert.easymapslib.util.PermissionUtils
import com.location.movetracker.database.LocationHistoryDatabase
import com.location.movetracker.ui.EasyMapsActivity
import com.location.movetracker.util.Coroutines
import com.location.movetracker.util.DataManager
import kotlinx.coroutines.Job


class BackgroundLocationTrackingService : LifecycleService() {
    private val CHANNEL_ID = "ForegroundServiceChannel"

    private val db by lazy { LocationHistoryDatabase.invoke(this) }

    /**
     * Location Request and Client
     */
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { createLocationRequest() }

    /**
     * Location Settings
     */
    private val locationSettingsBuilder by lazy {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    }

    private val settingsClient by lazy { LocationServices.getSettingsClient(this) }

    /**
     * Callback
     */
    private val fineLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.let {
                saveLocation(it.lastLocation.latitude, it.lastLocation.longitude)
            }
        }
    }

    private fun saveLocation(latitude: Double, longitude: Double): Job {
        return Coroutines.io {
            DataManager.saveLocation(
                db,
                latitude,
                longitude
            )
        }
    }

    /**
     * Creates a LocationRequest model
     */
    private fun createLocationRequest(): LocationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Checks runtime permission first.
     * Then check if GPS settings is enabled by user
     * If all good, then start listening user location
     * and update livedata
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (PermissionUtils.isLocationPermissionsGranted(this).not())
            return

        val settingsTask = settingsClient.checkLocationSettings(locationSettingsBuilder.build())
        settingsTask.addOnSuccessListener {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let { saveLocation(it.latitude, it.longitude) }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    fineLocationCallback,
                    null
                )
            }

        }
    }

    /**
     * Removes listener onInactive
     */
    private fun stopLocationUpdates() =
        fusedLocationClient.removeLocationUpdates(fineLocationCallback)


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val notificationIntent = Intent(this, EasyMapsActivity::class.java)
        createNotificationChannel()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking Your Location")
            .setContentText("Storing it")
            .setSmallIcon(R.drawable.ic_icon_marker)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        startLocationUpdates()

        return Service.START_REDELIVER_INTENT
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }
}
