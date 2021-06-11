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
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.SelectedPlayerAdapter
import de.fbirk.doubleout.model.Player.Player

class GameStartAddPlayerFragment : Fragment() {

    private val viewModel: GameStartViewModel by activityViewModels()
    private var selectedPlayerAdapter: SelectedPlayerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_start_add_player, container, false)
        val addPlayerButton = view.findViewById<Button>(R.id.btn_gameStart_addPlayer)
        val selectedPlayers = viewModel.selectedPlayers
        val allPlayers: Array<String> =
            viewModel.allPlayers.value!!.map { player -> player.name }.toTypedArray()

        val autoCompleteTextNewPlayerName =
            view.findViewById<AutoCompleteTextView>(R.id.txt_selectPlayer_newPlayerName)
        val autoCompleteAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(view.context, android.R.layout.select_dialog_item, allPlayers)
        autoCompleteTextNewPlayerName.threshold = 1
        autoCompleteTextNewPlayerName.setAdapter(autoCompleteAdapter)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyler_selectedPlayersView)
        selectedPlayerAdapter = SelectedPlayerAdapter(this, selectedPlayers)
        recyclerView.adapter = selectedPlayerAdapter

        addPlayerButton.setOnClickListener {
            val playerName: String = autoCompleteTextNewPlayerName.editableText.toString()
            if (allPlayers.contains(playerName)) {
                // TODO: fetch Player Object from DBO

                val list = selectedPlayers.value!!
                list.plus(Player(0, 0, 0, 0.0, playerName))
                selectedPlayers.postValue(list)
            } else {
                // new Player
                if (selectedPlayers.value != null) {
                    val list = selectedPlayers.value!!

                    list.plus(Player(allPlayers.size + 1, 0, 0, 0.0, playerName))
                    selectedPlayers.postValue(list)
                    notifyAdapter(selectedPlayers)
                } else {
                    val list = listOf(Player(allPlayers.size + 1, 0, 0, 0.0, playerName))
                    selectedPlayers.postValue(list)
                    notifyAdapter(selectedPlayers)
                }
            }
        }
        return view
    }

    private fun notifyAdapter(list: MutableLiveData<List<Player>>){
        selectedPlayerAdapter!!.items = list
    }
}