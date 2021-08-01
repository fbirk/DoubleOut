package de.fbirk.doubleout.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.TimeLineAdapter
import de.fbirk.doubleout.model.TimeLineModel

class GameStatsTimelineFragment : Fragment() {

    private lateinit var mAdapter: TimeLineAdapter
    private val mDataList = ArrayList<TimeLineModel>()
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_stats_timeline, container, false)

        setDataListItems()

        mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = TimeLineAdapter(mDataList)
        recyclerView.adapter = mAdapter


        return view
    }

    private fun setDataListItems() {
        mDataList.add(
            TimeLineModel(
                "Wright vs. Van Gerwen",
                "2020-01-01 12:00",
                "Won: 7:3, Avg.: 39.5"
            )
        )
        mDataList.add(
            TimeLineModel(
                "Wright vs. Alcinas",
                "2018-12-13 12:00",
                "Lost: 1:3, Avg.: 35.0"
            )
        )
        mDataList.add(
            TimeLineModel(
                "Wright vs. Lewis",
                "2017-12-14 12:00",
                "Lost: 1:4, Avg.: 34.8"
            )
        )
    }
}