package de.fbirk.doubleout.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.SelectedPlayerAdapter
import de.fbirk.doubleout.model.Player.Player

class GameStartAddPlayerFragment : Fragment() {

    private val viewModel: GameStartViewModel by activityViewModels()
    private var selectedPlayerAdapter: SelectedPlayerAdapter? = null
    private var _selectedPlayers: ArrayList<Player> = ArrayList<Player>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_start_add_player, container, false)
        val allPlayers: Array<String> =
            viewModel.allPlayers.value!!.map { player -> player.name }.toTypedArray()

        val autoCompleteTextNewPlayerName =
            view.findViewById<AutoCompleteTextView>(R.id.txt_selectPlayer_newPlayerName)
        val autoCompleteAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(view.context, android.R.layout.select_dialog_item, allPlayers)
        autoCompleteTextNewPlayerName.threshold = 1
        autoCompleteTextNewPlayerName.setAdapter(autoCompleteAdapter)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyler_selectedPlayersView)
        selectedPlayerAdapter = SelectedPlayerAdapter(this, _selectedPlayers)
        recyclerView.adapter = selectedPlayerAdapter

        viewModel.selectedPlayers.observe(viewLifecycleOwner, Observer {
            _selectedPlayers.removeAll(_selectedPlayers)
            _selectedPlayers.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        view.findViewById<Button>(R.id.btn_gameStart_addPlayer)
            .setOnClickListener {
                addPlayerEvent(autoCompleteTextNewPlayerName)
            }

        return view
    }

    private fun addPlayerEvent(autoCompleteTextNewPlayerName: AutoCompleteTextView) {
        val playerName: String = autoCompleteTextNewPlayerName.editableText.toString()
        // Fetch already known players
        viewModel.addPlayer(Player(0, 0, 0, 0.0, playerName))
    }
}