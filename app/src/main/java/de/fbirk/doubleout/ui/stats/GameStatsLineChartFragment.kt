package de.fbirk.doubleout.ui.stats

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


import androidx.core.content.ContextCompat

import android.graphics.drawable.Drawable

import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.Utils
import de.fbirk.doubleout.R


/**
 * A simple [Fragment] subclass.
 * Use the [GameStatsLineChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameStatsLineChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_stats_linechart, container, false)

        val mChart = view.findViewById<LineChart>(R.id.chart_lineChart)
        mChart.setTouchEnabled(true)
        mChart.setPinchZoom(true)
        mChart.description.isEnabled = false

        var entries: ArrayList<Entry> = arrayListOf()
        entries.add(Entry(1f, 43.4f))
        entries.add(Entry(2f, 42.1f))
        entries.add(Entry(3f, 45.6f))
        entries.add(Entry(4f, 53.2f))
        entries.add(Entry(5f, 78.9f))
        entries.add(Entry(6f, 77.2f))
        entries.add(Entry(7f, 80.1f))

        val set1: LineDataSet
        if (mChart.getData() != null &&
            mChart.getData().getDataSetCount() > 0
        ) {
            set1 = mChart.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = entries
            mChart.getData().notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(entries, getString(R.string.chart_line_title))
            set1.setDrawIcons(false)
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.DKGRAY
            set1.setCircleColor(Color.DKGRAY)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            if (Utils.getSDKInt() >= 18) {

            } else {
                set1.fillColor = Color.DKGRAY
            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1)
            val data = LineData(dataSets)
            mChart.setData(data)
        }

        return view
    }
}