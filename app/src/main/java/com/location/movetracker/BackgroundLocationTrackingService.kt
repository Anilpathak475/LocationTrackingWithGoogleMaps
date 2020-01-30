package com.location.movetracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.location.movetracker.database.LocationHistoryDatabase
import com.location.movetracker.location.livedata.LocationLiveData
import com.location.movetracker.ui.EasyMapsActivity


class BackgroundLocationTrackingService : LifecycleService() {
    private val CHANNEL_ID = "ForegroundServiceChannel"

    private lateinit var locationLiveData: LocationLiveData
    private val db by lazy { LocationHistoryDatabase.invoke(this) }
    override fun onCreate() {
        super.onCreate()

    }

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
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}
