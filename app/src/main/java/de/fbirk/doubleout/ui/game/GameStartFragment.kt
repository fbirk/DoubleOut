package de.fbirk.doubleout.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import de.fbirk.doubleout.R


class GameStartFragment : Fragment() {

    private val viewModel: GameStartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mViewPager = view.findViewById<ViewPager2>(R.id.pager_gameStart_viewPager)
        mViewPager.adapter = activity?.let { ViewPagerAdapter(it) }
    }
}

