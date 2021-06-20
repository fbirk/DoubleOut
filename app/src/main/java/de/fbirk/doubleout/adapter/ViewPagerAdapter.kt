package de.fbirk.doubleout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.fbirk.doubleout.ui.game.GameStartAddPlayerFragment
import de.fbirk.doubleout.ui.game.GameStartSettings

class ViewPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {

    var mFragments = arrayOf(GameStartAddPlayerFragment(), GameStartSettings())

    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}