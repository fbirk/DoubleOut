package de.fbirk.doubleout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.fbirk.doubleout.ui.game.start.GameStartAddPlayerFragment
import de.fbirk.doubleout.ui.game.start.GameStartSettings

class GameStartViewPagerAdapter(fa:FragmentActivity, fragments:Array<Fragment>) : FragmentStateAdapter(fa) {

    private var mFragments = fragments

    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    fun getFragments(): Array<Fragment>{
        return mFragments
    }
}