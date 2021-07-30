package de.fbirk.doubleout.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.viewpager2.widget.ViewPager2
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.GameStartViewPagerAdapter

class GameStatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_stats_main_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mViewPager = view.findViewById<ViewPager2>(R.id.pager_gameStats_viewPager)
        mViewPager.adapter = activity?.let {
            GameStartViewPagerAdapter(
                it,
                arrayOf(GameStatsTimelineFragment(), GameStatsLineChartFragment(), GameStatsPieChartFragment())
            )
        }

        val spinnerPlayerSelection = view.findViewById<Spinner>(R.id.spinner_statsPlayerSelector)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dummy_spinner_values,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPlayerSelection.adapter = adapter
        }

        val callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        }

        mViewPager.registerOnPageChangeCallback(callback)
    }
}