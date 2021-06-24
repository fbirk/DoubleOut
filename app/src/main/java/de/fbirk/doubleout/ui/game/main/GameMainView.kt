package de.fbirk.doubleout.ui.game.main

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.fbirk.doubleout.R
import java.lang.Math.*


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