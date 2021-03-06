package de.fbirk.doubleout.ui.game.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fbirk.doubleout.R

/**
 * A simple [Fragment] subclass.
 * Use the [GameStartSelectedPlayerElement.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameStartSelectedPlayerElement : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(
            R.layout.fragment_game_start_selected_player_element,
            container,
            false
        )
        return view
    }
}