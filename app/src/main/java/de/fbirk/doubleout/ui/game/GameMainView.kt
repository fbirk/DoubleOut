package de.fbirk.doubleout.ui.game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fbirk.doubleout.R

class GameMainView : Fragment() {

    companion object {
        fun newInstance() = GameMainView()
    }

    private lateinit var viewModel: GameMainViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_main_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameMainViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}