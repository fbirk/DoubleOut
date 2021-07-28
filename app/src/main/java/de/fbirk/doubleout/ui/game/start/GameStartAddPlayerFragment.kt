package de.fbirk.doubleout.ui.game.start

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.SelectedPlayerAdapter
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Fragment for the add player screen (1.1)
 * Manages fetching of all known players for dropdown selection
 * and adding new players to the list view.
 */
class GameStartAddPlayerFragment : Fragment() {

    private lateinit var viewModel: GameStartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, GameStartViewModelFactory(context)).get(GameStartViewModel(context)::class.java)
    }

    private var selectedPlayerAdapter: SelectedPlayerAdapter? = null
    private var _selectedPlayers: ArrayList<Player> = ArrayList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_start_add_player, container, false)
        val allPlayers = arrayListOf("")

        val autoCompleteTextNewPlayerName =
            view.findViewById<AutoCompleteTextView>(R.id.txt_selectPlayer_newPlayerName)
        val autoCompleteAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(view.context, android.R.layout.select_dialog_item, allPlayers)
        autoCompleteTextNewPlayerName.threshold = 1
        autoCompleteTextNewPlayerName.setAdapter(autoCompleteAdapter)

        viewModel.allPlayers.observe(viewLifecycleOwner) { players ->
            players.let {
                autoCompleteAdapter.clear()
                val playerList = players.map { player -> player.name }.toMutableList()
                autoCompleteAdapter.addAll(playerList)
            }
        }

        autoCompleteTextNewPlayerName.setOnTouchListener(
            View.OnTouchListener { v, event ->
                autoCompleteTextNewPlayerName.showDropDown()
                return@OnTouchListener false
            })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyler_selectedPlayersView)
        selectedPlayerAdapter = SelectedPlayerAdapter(this, _selectedPlayers)
        recyclerView.adapter = selectedPlayerAdapter

        // observe player list for changes to update list view
        viewModel.selectedPlayers.observe(viewLifecycleOwner, {
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

                // hide keyboard after input
                view.hideKeyboard()
            }

        return view
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Add a new player to the data storage
     */
    private fun addPlayerEvent(text: String) {
        val playerName: String = text
        // TODO: Fetch already known players and check for duplicates with new player
        viewModel.addPlayer(Player(0, 0, 0, 0, 0.0, playerName))
    }
}