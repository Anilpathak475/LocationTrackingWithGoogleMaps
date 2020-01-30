package com.location.movetracker.util

import android.util.Log
import androidx.annotation.WorkerThread
import com.location.movetracker.database.LocationHistory
import com.location.movetracker.database.LocationHistoryDatabase

object DataManager {

    @WorkerThread
    fun saveLocation(db: LocationHistoryDatabase, lat: Double, long: Double) {
        val locationHistory = LocationHistory(lat, long)
        db.locationHistoryDoa().insert(locationHistory)
        Log.d("Size in databse", "" + getLocationHistory(db).size)
    }

    @WorkerThread
    fun getLocationHistory(db: LocationHistoryDatabase): List<LocationHistory> {
        return db.locationHistoryDoa().getAll()
    }

    @WorkerThread
    private fun clearLocationHistory(db: LocationHistoryDatabase) {
        return db.locationHistoryDoa().deleteAll()
    }

}