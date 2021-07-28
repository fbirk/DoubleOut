package de.fbirk.doubleout.ui.game.start

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import kotlinx.coroutines.launch

class GameStartViewModel(application: Context) : ViewModel() {
    var selectedPlayers = MutableLiveData<ArrayList<Player>>()
    var allPlayers: LiveData<List<Player>>
    private val repository: PlayerRepository

    init {
        val dao = PlayerDatabase.getInstance(application).playerDao()
        repository = PlayerRepository(dao)
        allPlayers = repository.getAllPlayers()
    }

    fun addPlayer(player: Player) = viewModelScope.launch {
        if (selectedPlayers.value != null && selectedPlayers.value?.contains(player) == false) {
            val currentPlayers = selectedPlayers.value

            if (currentPlayers != null) {
                repository.savePlayer(player)
                currentPlayers.add(player)
                //selectedPlayers.postValue(currentPlayers!!)
                selectedPlayers.value = currentPlayers!!
            }
        } else {
            val playerList = arrayListOf(player)
            //selectedPlayers.postValue(playerList)
            selectedPlayers.value = playerList
        }
    }
}

class GameStartViewModelFactory(private val application: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameStartViewModel::class.java)) {
            return GameStartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
