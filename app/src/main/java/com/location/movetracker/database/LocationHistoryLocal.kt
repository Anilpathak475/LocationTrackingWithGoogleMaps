package com.location.movetracker.database


import androidx.annotation.Nullable
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity
data class LocationHistory(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    @ColumnInfo(name = "isUpdated")
    @Nullable
    val isUpdated: Boolean = false,

    @ColumnInfo(name = "lat")
    @SerializedName("wish_id")
    val wishId: String,

    @ColumnInfo(name = "long")
    @SerializedName("long")
    val long: String
)

@Dao
interface LocationHistoryDoa : BaseDao<LocationHistory> {

    @Query("SELECT * FROM locationhistory")
    fun getAll(): List<LocationHistory>

    @Query("DELETE FROM locationhistory")
    fun deleteAll()

}


