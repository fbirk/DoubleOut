package de.fbirk.doubleout.ui.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {

    var mFragments = arrayOf(GameStartAddPlayerFragment(), GameStartSettings())

    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}