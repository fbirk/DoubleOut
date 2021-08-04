package de.fbirk.doubleout.ui.stats

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.fbirk.doubleout.GameStatsHeatmapFragment
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.GameStartViewPagerAdapter
import de.fbirk.doubleout.model.Player.Player

class GameStatsFragment : Fragment() {

    private lateinit var viewModel: GameStatisticsViewModel
    private var allPlayers = ArrayList<Player>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            this,
            GameStatisticsViewModelFactory(context)
        ).get(GameStatisticsViewModel::class.java)
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
        val tabLayout = view.findViewById<TabLayout>(R.id.pager_tablayout)
        mViewPager.adapter = activity?.let {
            GameStartViewPagerAdapter(
                it,
                arrayOf(
                    GameStatsTimelineFragment(),
                    GameStatsLineChartFragment(),
                    GameStatsHeatmapFragment(),
                    GameStatsPieChartFragment()
                )
            )
        }
        TabLayoutMediator(tabLayout, mViewPager) {tab, pos ->

        }.attach()

        val allPlayerNames = arrayListOf("")
        val spinnerPlayerSelection = view.findViewById<Spinner>(R.id.spinner_statsPlayerSelector)
        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, allPlayerNames)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPlayerSelection.adapter = adapter
            }

        val callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        }

        viewModel.allPlayers.observe(viewLifecycleOwner, {
            adapter.clear()
            val names = it.map { p -> p.name }.toList()
            allPlayers.addAll(it)
            adapter.addAll(names)
        })

        mViewPager.registerOnPageChangeCallback(callback)
    }
}