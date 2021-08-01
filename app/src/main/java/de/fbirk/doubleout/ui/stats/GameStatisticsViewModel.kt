package de.fbirk.doubleout.ui.stats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import java.lang.IllegalArgumentException

class GameStatisticsViewModel(private val context: Context) : ViewModel() {

    private val repository: PlayerRepository
    lateinit var allPlayers : LiveData<List<Player>>

    init {
        val dao = PlayerDatabase.getInstance(context).playerDao()
        repository = PlayerRepository(dao)
        Thread {
            allPlayers = repository.getAllPlayers()
        }.start()
    }
}

class GameStatisticsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameStatisticsViewModel::class.java)) {
            return GameStatisticsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}