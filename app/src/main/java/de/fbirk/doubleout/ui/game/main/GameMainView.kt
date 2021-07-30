package de.fbirk.doubleout.ui.game.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.ActivePlayerAdapter
import de.fbirk.doubleout.model.Player.Player


class GameMainView : Fragment(), GameFinishedDialogFragment.GameFinishedDialogListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            this,
            GameMainViewViewModelFactory(context)
        ).get(GameMainViewViewModel::class.java)

    }

    private lateinit var viewModel: GameMainViewViewModel

    private lateinit var _activePlayers: ArrayList<Player>
    private var activePlayerAdapter: ActivePlayerAdapter? = null
    private val navArgs: GameMainViewArgs by navArgs()
    private var currentPlayer: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_main_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentThrowsOverview = view.findViewById<TextView>(R.id.txt_currentThrowsOverview)
        val activePlayerName = view.findViewById<TextView>(R.id.txt_currentPlayerName)
        val activePlayerPoints = view.findViewById<TextView>(R.id.txt_currentPlayerPoints)
        val missedButton = view.findViewById<Button>(R.id.btn_missed)
        val dartBoardView = view.findViewById<DartboardView>(R.id.main_dartboard_view)
        val segmentView = view.findViewById<DartboardSegmentView>(R.id.main_dartboard_segmentView)

        _activePlayers = viewModel.initializeWithPlayers(navArgs.playerNames, navArgs.startingValue)
        activePlayerName.text = _activePlayers[0].name
        activePlayerPoints.text = _activePlayers[0].pointsLeft.toString()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_activePlayers)
        activePlayerAdapter = ActivePlayerAdapter(this, _activePlayers!!)
        recyclerView.adapter = activePlayerAdapter

        // observe players list
        viewModel.players.observe(viewLifecycleOwner, {
            // on new value, update local players list and notify recyclerview
            _activePlayers.removeAll(_activePlayers)
            _activePlayers.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()

            // update display information (text over dartboard)
            if (viewModel.currentIndex == currentPlayer) {
                activePlayerPoints.text = _activePlayers[currentPlayer].pointsLeft.toString()
            } else {
                currentPlayer = viewModel.currentIndex
                activePlayerPoints.text = _activePlayers[currentPlayer].pointsLeft.toString()
                activePlayerName.text = _activePlayers[currentPlayer].name
            }
        })

        viewModel.hasWinner.observe(viewLifecycleOwner, {
            if (it) {
                val dialog = GameFinishedDialogFragment()
                fragmentManager?.let { it1 -> dialog.show(it1, "GameFinishedDialog") }
            }
        })

        // click listener for dartboard fifth -> on click show segment view for selected fifth
        dartBoardView.onDartboardFifthClicked = fun(selectedFifth) {
            segmentView.setSelectedSegmentIndex(selectedFifth)
            segmentView.isVisible = true
        }

        // on segment-field clicked add points to current player in view model and update display texts
        segmentView.onDartboardSegmentClicked = fun(resultField) {
            segmentView.isVisible = false   // hide segment view

            viewModel.addPoints(resultField)
            if (viewModel.currentIndex != currentPlayer) {
                currentPlayer = viewModel.currentIndex
                activePlayerPoints.text = _activePlayers[currentPlayer].pointsLeft.toString()
                activePlayerName.text = _activePlayers[currentPlayer].name
            }

            currentThrowsOverview.text = viewModel.currentThrowsInTurn
        }

        // on click listener for missed button -> 0 points for current throw
        missedButton.setOnClickListener {
            viewModel.missed()
            segmentView.isVisible = false
            currentThrowsOverview.text = viewModel.currentThrowsInTurn
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val currentThrowsOverview = view?.findViewById<TextView>(R.id.txt_currentThrowsOverview)

        viewModel.resetPlayers()
        currentThrowsOverview?.text = ""
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        this.findNavController().navigate(R.id.action_gameMainView_to_mainFragment)
    }
}