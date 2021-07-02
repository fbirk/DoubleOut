package de.fbirk.doubleout.ui.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.fbirk.doubleout.R


class GameMainView : Fragment()  {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dartBoardView = view.findViewById<DartboardView>(R.id.main_dartboard_view)
        dartBoardView.onDartboardFifthClicked = fun(x) {
            when (x) {
                0 -> println("Top Right")
                1 -> println("Bottom Right")
                2 -> println("Bottom Left")
                3 -> println("Top Left")
                4 -> println("Middle")
            }
        }
    }

}