package de.fbirk.doubleout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.hss.heatmaplib.HeatMap
import de.fbirk.doubleout.model.Field
import de.fbirk.doubleout.model.FieldPosition
import java.lang.Math.cos
import java.util.*
import kotlin.math.abs

class GameStatsHeatmapFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_stats_heatmap, container, false)

        var heatmap = view.findViewById<HeatMap>(R.id.heatmap)
        heatmap.setMinimum(0.0)
        heatmap.setMaximum(100.0)

        var max = 0.0
        var min = 0.0

        for (i in 0..1000) {
            val pointPair =
                fieldToPoint(Field((1..20).random(), (1..3).random(), FieldPosition.UPPER))

            if (max < pointPair.first) {
                max = pointPair.first
            }
            if (max < pointPair.second) {
                max = pointPair.second
            }
            if (min > abs(pointPair.first)) {
                min = abs(pointPair.first)
            }
            if (min > abs(pointPair.second)) {
                min = abs(pointPair.second)
            }
        }
        for (i in 0..100) {
            val pointPair =
                fieldToPoint(Field((1..20).random(), (1..3).random(), FieldPosition.UPPER))
            val x = (abs(pointPair.first) - min) / (max - min)
            val y = (abs(pointPair.second) - min) / (max - min)
            val point =
                HeatMap.DataPoint(x.toFloat(), y.toFloat(), 100.0)
            heatmap.addData(point)
        }

        return view
    }

    private fun pointToDegree(point: Int): Int {
        return when (point) {
            6 -> 0
            10 -> 18
            15 -> 36
            2 -> 54
            17 -> 72
            3 -> 90
            19 -> 108
            7 -> 126
            16 -> 144
            8 -> 162
            11 -> 180
            14 -> 198
            9 -> 216
            12 -> 234
            5 -> 252
            20 -> 270
            1 -> 288
            18 -> 306
            4 -> 324
            13 -> 342
            else -> 360
        }
    }

    private fun fieldToPoint(field: Field): Pair<Double, Double> {
        var x = (0 + radiusByFactor(
            field.factor,
            field.pos
        )) * kotlin.math.cos(pointToDegree(field.value).toDouble())
        var y = (0 + radiusByFactor(
            field.factor,
            field.pos
        )) * kotlin.math.sin(pointToDegree(field.value).toDouble())
        return Pair(x+700, y+700)
    }

    private fun radiusByFactor(factor: Int, pos: FieldPosition): Int {
        var rad = 0
        if (factor == 1) {
            if (pos == FieldPosition.LOWER) {
                rad = 50
            } else {
                rad = 100
            }
        } else if (factor == 2) {
            rad = 200
        } else {
            rad = 350
        }
        return rad
    }

}