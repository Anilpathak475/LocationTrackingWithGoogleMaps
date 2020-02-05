package com.location.movetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.location.movetracker.database.LocationHistory
import com.location.movetracker.database.LocationHistoryDatabase
import com.location.movetracker.util.Coroutines
import de.savedroid.ui.recyclerview.CellRecyclerAdapter
import kotlinx.android.synthetic.main.activity_history_viewer_activty.*

class HistoryViewerActivty : AppCompatActivity() {

    private val db by lazy { LocationHistoryDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_viewer_activty)
        Coroutines.ioThenMain({
            db.locationHistoryDoa().getAll()
        }, {
            it?.let {
                val listItems = it.map {
                    LocationSessionCell(it) { locationSessionCell ->
                        selectedLocationSession(it)
                    }
                }
                val adapter = CellRecyclerAdapter(listItems)
                recyclerView.adapter = adapter
            }
        })

    }


    fun selectedLocationSession(locationHistory: LocationHistory) {}
}
