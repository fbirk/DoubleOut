package de.fbirk.doubleout.ui.stats

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import de.fbirk.doubleout.R

class GameStatsPieChartFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_stats_pie_chart, container, false)

        val pieChart = view.findViewById<PieChart>(R.id.chart_pieChart)

        val xvalues = ArrayList<PieEntry>()
        var dummyValues = 0
        for (i in 0..20) {
            var randNum = (0..12).random()
            dummyValues += randNum
            if (dummyValues > 100) {
                randNum = 0
            }
            if (i == 20 && dummyValues < 100) {
                randNum = 100 - dummyValues
            }
            xvalues.add(PieEntry((randNum).toFloat(), "$i"))
        }
        val dataSet = PieDataSet(xvalues, "Distribution of thrown fields")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val data = PieData(dataSet)
        // In Percentage
        data.setValueFormatter(PercentFormatter())

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        data.setValueTextSize(0f)

        chartDetails(pieChart, Typeface.SANS_SERIF)

        return view
    }

    fun chartDetails(mChart: PieChart, tf: Typeface) {
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(7f);
        mChart.setTransparentCircleRadius(10f);

        mChart.description.isEnabled = true
        mChart.centerText = ""
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextTypeface(tf)
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.formSize = 20F
        l.formToTextSpace = 5f
        l.form = Legend.LegendForm.SQUARE
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        mChart.setTouchEnabled(false)
        mChart.setDrawEntryLabels(false)
        mChart.legend.isWordWrapEnabled = true
        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setUsePercentValues(true)
        mChart.setDrawCenterText(false)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = false
    }
}