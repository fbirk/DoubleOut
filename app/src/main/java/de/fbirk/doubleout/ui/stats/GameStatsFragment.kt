package de.fbirk.doubleout.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.GameStatsViewPagerAdapter
import de.fbirk.doubleout.model.Player.Player

class GameStatsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_stats_main_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mViewPager = view.findViewById<ViewPager2>(R.id.pager_gameStats_viewPager)
        mViewPager.adapter = activity?.let { GameStatsViewPagerAdapter(it) }

        val lineChart = view.findViewById<LineChart>(R.id.chart_lineChart)
        fillLineChartWithData(lineChart)
    }

    private fun fillLineChartWithData(lineChart: LineChart?) {
        val dataObjects: List<Player>
        var entries: ArrayList<Entry> = arrayListOf()
        entries.add(Entry(43.4f,1f))
        entries.add(Entry(42.1f,2f))
        entries.add(Entry(45.6f,3f))
        entries.add(Entry(53.2f,4f))
        entries.add(Entry(78.9f,5f))
        entries.add(Entry(77.2f,6f))
        entries.add(Entry(80.1f,7f))

        val dataSet = LineDataSet(entries, "Avg. over time")
        dataSet.setColor(134,1)
        dataSet.valueTextColor = 100

        val lineData = LineData(dataSet)
        lineChart?.data = lineData
        lineChart?.invalidate()
    }

}