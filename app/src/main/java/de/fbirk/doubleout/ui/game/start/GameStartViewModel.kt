package de.fbirk.doubleout.ui.game.start

import android.content.Context
import androidx.lifecycle.*
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameStartViewModel(application: Context) : ViewModel() {
    var selectedPlayers = MutableLiveData<ArrayList<Player>>()
    var selectedPlayerIds = MutableLiveData<IntArray>()
    lateinit var allPlayers: LiveData<List<Player>>
    private val repository: PlayerRepository

    init {
        val dao = PlayerDatabase.getInstance(application).playerDao()
        repository = PlayerRepository(dao)
        Thread {
            allPlayers = repository.getAllPlayers()
        }.start()
    }

    fun addPlayer(player: Player) = viewModelScope.launch {
        if (allPlayers.value?.map { x -> x.name }?.contains(player.name) == true) {
            Thread {
                val mPlayer = repository.getPlayerByName(player.name)
                mPlayer.value?.let { addPlayerToSelectedPlayers(it) }
            }.start()
        } else {
            Thread { repository.savePlayer(player) }.start()
        }
        addPlayerToSelectedPlayers(player)
    }

    private fun addPlayerToSelectedPlayers(player: Player) {
        if (selectedPlayers.value != null && selectedPlayers.value?.contains(player) == false) {
            val currentPlayers = selectedPlayers.value

            if (currentPlayers != null) {
                currentPlayers.add(player)
                selectedPlayers.value = currentPlayers!!
            }
        } else {
            selectedPlayers.value = arrayListOf(player)
        }
    }

    suspend fun getSelectedPlayerIds(coroutineScope: CoroutineScope): IntArray {
        var idList = ArrayList<Int>()

        coroutineScope.launch {

            for (player in selectedPlayers.value!!) {
                // val all = repository.getAllPlayers()
                val id = repository.getPlayerByName(player.name).value?.number
                if (id != null) {
                    idList.add(id)
                }
            }

            selectedPlayerIds.postValue(idList.toIntArray())
        }.join()
        return idList.toIntArray()
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
