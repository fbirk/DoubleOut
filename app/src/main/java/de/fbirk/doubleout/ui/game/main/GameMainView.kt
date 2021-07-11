package de.fbirk.doubleout.ui.game.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dartBoardView = view.findViewById<DartboardView>(R.id.main_dartboard_view)
        val segmentView = view.findViewById<DartboardSegmentView>(R.id.main_dartboard_segmentView)

        dartBoardView.onDartboardFifthClicked = fun(selectedFifth) {
            println(selectedFifth)
            segmentView.setSelectedSegmentIndex(selectedFifth)
            segmentView.isVisible = true
        }

        segmentView.onDartboardSegmentClicked = fun(resultField) {
            var toast = Toast.makeText(
                activity,
                resultField.factor.toString() + "x " + resultField.value,
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.TOP, 0, 30)
            toast.show()

            segmentView.isVisible = false
        }
    }
}