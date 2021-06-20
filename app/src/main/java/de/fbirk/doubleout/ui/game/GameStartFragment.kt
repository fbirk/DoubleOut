package de.fbirk.doubleout.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.ViewPagerAdapter


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

        var mGameStartBackButton = view.findViewById<Button>(R.id.btn_gameStart_back)
        mGameStartBackButton.setOnClickListener {
            val current = mViewPager.currentItem - 1
            if (current >= 0) {
                mViewPager.currentItem = current
            }
        }

        var mGameStartNextButton = view.findViewById<Button>(R.id.btn_gameStart_next)
        mGameStartNextButton.setOnClickListener {
            val current = mViewPager.currentItem + 1
            if (current < mViewPager.adapter?.itemCount ?: -1) {
                mViewPager.currentItem = current
            }
        }

        var callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mGameStartNextButton.text = getString(R.string.btn_slide_next_text)
                    }
                    1 -> {
                        mGameStartNextButton.text = getString(R.string.btn_slide_next_text_alt)
                    }
                }

                if (position > 0) {
                    mGameStartBackButton.visibility = View.VISIBLE
                } else {
                    mGameStartBackButton.visibility = View.INVISIBLE
                }
            }
        }

        mViewPager.registerOnPageChangeCallback(callback)
    }
}

