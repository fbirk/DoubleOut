package de.fbirk.doubleout.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fbirk.doubleout.R

/**
 * A simple [Fragment] subclass.
 * Use the [GameStatsLineChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameStatsLineChartFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_stats_linechart, container, false)
    }
}