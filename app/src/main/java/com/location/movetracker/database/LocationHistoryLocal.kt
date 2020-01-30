package com.location.movetracker.database


import androidx.annotation.Nullable
import androidx.room.*

@Entity
data class LocationHistory(
    @ColumnInfo(name = "lat")
    @Nullable
    var lat: Double? = 0.0,

    @ColumnInfo(name = "long")
    @Nullable
    var long: Double? = 0.0
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "isUpdated")
    @Nullable
    var isUpdated: Boolean? = false
}

@Dao
interface LocationHistoryDoa : BaseDao<LocationHistory> {

    @Query("SELECT * FROM locationhistory")
    fun getAll(): List<LocationHistory>

    @Query("DELETE FROM locationhistory")
    fun deleteAll()

}


