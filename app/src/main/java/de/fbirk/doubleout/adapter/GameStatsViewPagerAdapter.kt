package de.fbirk.doubleout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.fbirk.doubleout.ui.stats.GameStatsLineChartFragment

class GameStatsViewPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {

    private var mFragments = arrayOf(GameStatsLineChartFragment())

    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}