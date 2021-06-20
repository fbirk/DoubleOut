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

/**
 * Fragment for the add player screen (1.1)
 * Manages fetching of all known players for dropdown selection
 * and adding new players to the list view.
 */
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

        autoCompleteTextNewPlayerName.setOnTouchListener(
            View.OnTouchListener { v, event ->
                autoCompleteTextNewPlayerName.showDropDown()
                return@OnTouchListener false
            })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyler_selectedPlayersView)
        selectedPlayerAdapter = SelectedPlayerAdapter(this, _selectedPlayers)
        recyclerView.adapter = selectedPlayerAdapter

        // observe player list for changes to update list view
        viewModel.selectedPlayers.observe(viewLifecycleOwner, Observer {
            _selectedPlayers.removeAll(_selectedPlayers)
            _selectedPlayers.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        // add current selected/added player to the data storage and list view
        view.findViewById<Button>(R.id.btn_gameStart_addPlayer)
            .setOnClickListener {
                // add to list
                addPlayerEvent(autoCompleteTextNewPlayerName.editableText.toString())
                // clear text field afterwards
                autoCompleteTextNewPlayerName.text.clear()
            }

        return view
    }

    /**
     * Add a new player to the data storage
     */
    private fun addPlayerEvent(text: String) {
        val playerName: String = text
        // TODO: Fetch already known players and check for duplicates with new player
        viewModel.addPlayer(Player(0, 0, 0, 0.0, playerName))
    }
}