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
        mDataList.add(TimeLineModel("Item successfully delivered", ""))
        mDataList.add(TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00"))
        mDataList.add(TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00"))
        mDataList.add(TimeLineModel("Item has been given to the courier", "2017-02-11 18:00"))
        mDataList.add(TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30"))
        mDataList.add(TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00"))
        mDataList.add(TimeLineModel("Order processing initiated", "2017-02-10 15:00"))
        mDataList.add(TimeLineModel("Order confirmed by seller", "2017-02-10 14:30"))
        mDataList.add(TimeLineModel("Order placed successfully", "2017-02-10 14:00"))
    }
}