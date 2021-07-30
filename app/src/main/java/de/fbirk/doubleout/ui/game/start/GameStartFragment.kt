package de.fbirk.doubleout.ui.game.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.GameStartViewPagerAdapter

/**
 * Main Fragment for the Game Start screens.
 * Manages the viewPager and its button logic, also the navigation to the main game screen
 */
class GameStartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = arrayOf(GameStartAddPlayerFragment(), GameStartSettings())

        // Instantiate the viewPager to manage swipe between "addPlayer" and "gameSettings" screens
        val mViewPager = view.findViewById<ViewPager2>(R.id.pager_gameStart_viewPager)
        mViewPager.adapter = activity?.let {
            GameStartViewPagerAdapter(
                it, fragments
            )
        }

        // add action for viewPager navigate back button
        val mGameStartBackButton = view.findViewById<Button>(R.id.btn_gameStart_back)
        mGameStartBackButton.setOnClickListener {
            val current = mViewPager.currentItem - 1
            // if viewPager is not at first position move one position back
            if (current >= 0) {
                mViewPager.currentItem = current
            }
        }

        // add action for viewPager navigate next/start game button
        val mGameStartNextButton = view.findViewById<Button>(R.id.btn_gameStart_next)
        mGameStartNextButton.setOnClickListener {
            val current = mViewPager.currentItem + 1
            // move to next screen if not at last position
            if (current < mViewPager.adapter?.itemCount ?: -1) {
                mViewPager.currentItem = current
            } else {
                // move to main game screen
                if (current == mViewPager.adapter?.itemCount) {
                    val addPlayerFragment = fragments[0] as GameStartAddPlayerFragment

                    // add listener to navigate to next screen with id list, when callback returns
                    addPlayerFragment.onSelectedPlayerIdsPopulated = fun(ids) {
                        val action =
                            GameStartFragmentDirections.actionGameStartToGameMainView(ids)
                        findNavController().navigate(action)
                    }

                    // trigger callback to async get player ids
                    addPlayerFragment.getSelectedPlayerIds()
                }
            }
        }

        // define alternate callback on viewPager page change
        // rename buttons and hide/show buttons according to new viewPager position
        val callback = object : ViewPager2.OnPageChangeCallback() {
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

        // attach callback
        mViewPager.registerOnPageChangeCallback(callback)
    }
}

