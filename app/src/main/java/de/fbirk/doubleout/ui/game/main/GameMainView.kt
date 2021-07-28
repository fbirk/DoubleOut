package de.fbirk.doubleout.ui.game.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.adapter.ActivePlayerAdapter
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import de.fbirk.doubleout.ui.game.start.GameStartViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class GameMainView : Fragment() {

    companion object {
        fun newInstance() = GameMainView()
    }

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy {
        context?.let {
            PlayerDatabase.getInstance(
                it,
                applicationScope
            )
        }
    }
    private val repository by lazy { database?.let { PlayerRepository(it.playerDao()) } }

    private val viewModel: GameMainViewViewModel by viewModels {
        GameMainViewViewModelFactory(repository!!)
    }

    private var _activePlayers: ArrayList<Player> =
        arrayListOf(Player(0, 0, 0, 0, 0.0, "Tester"))
    private var activePlayerAdapter: ActivePlayerAdapter? = null
    private val selectedPlayers: GameMainViewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_main_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSelectedPlayersById(selectedPlayers.playerIDs)

        val missedButton = view.findViewById<Button>(R.id.btn_missed)
        val dartBoardView = view.findViewById<DartboardView>(R.id.main_dartboard_view)
        val segmentView = view.findViewById<DartboardSegmentView>(R.id.main_dartboard_segmentView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_activePlayers)
        activePlayerAdapter = ActivePlayerAdapter(this, _activePlayers!!)
        recyclerView.adapter = activePlayerAdapter

        viewModel.players.observe(viewLifecycleOwner, Observer {
            _activePlayers.removeAll(_activePlayers)
            _activePlayers.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        dartBoardView.onDartboardFifthClicked = fun(selectedFifth) {
            println(selectedFifth)
            segmentView.setSelectedSegmentIndex(selectedFifth)
            segmentView.isVisible = true
        }

        segmentView.onDartboardSegmentClicked = fun(resultField) {
            var toast = Toast.makeText(
                activity,
                resultField.factor.toString() + "x " + resultField.value,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.TOP, 0, 30)
            toast.show()

            viewModel.addPoints(resultField)

            segmentView.isVisible = false
        }

        missedButton.setOnClickListener {
            viewModel.missed()
        }
    }
}