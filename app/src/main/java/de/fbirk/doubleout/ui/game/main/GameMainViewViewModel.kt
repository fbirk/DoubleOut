package de.fbirk.doubleout.ui.game.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fbirk.doubleout.model.Field
import de.fbirk.doubleout.model.MatchSet
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerRepository
import de.fbirk.doubleout.model.Turn
import kotlin.collections.ArrayList

class GameMainViewViewModel(private val repository: PlayerRepository) : ViewModel() {
    var sets = MutableLiveData<List<MatchSet>>()
    var turns = MutableLiveData<ArrayList<Turn>>()
    var players = MutableLiveData<ArrayList<Player>>()
    private var currentIndex: Int = 0
    private var throwCounter: Int = 0


    private fun initializeWithPlayers(playerList: LiveData<List<Player>>) {
        players.value = playerList.value?.toCollection(ArrayList())
        for (player in players.value!!) {
            player.pointsLeft = 301
            player.currentAvg = 0.0
            player.currentPosition = 0
        }

        // sets.value = listOf(MatchSet(0, 0))
        // match.value = Match(0, Calendar.getInstance().time)
    }

    fun getSelectedPlayersById(selectedPlayerIds: IntArray) {
        if (selectedPlayerIds.isNotEmpty()) {
            val players = repository.getAllPlayersWithId(selectedPlayerIds.asList())
            initializeWithPlayers(players)
        } else {
            val players = repository.getAllPlayers()
            println(players.value)
            initializeWithPlayers(players)
        }
    }

    private fun nextPlayer() {
        if (currentIndex < players.value?.size!! - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
    }

    fun addPoints(field: Field) {
        if (players.value!![currentIndex].pointsLeft > (field.value * field.factor)) {
            //sets.value?.get(currentSet)?.turn?.add(Turn(field, players.value!![currentIndex]))
            players.value!![currentIndex].pointsLeft -= (field.value * field.factor)
        }

        throwCounter++

        if (throwCounter % 3 == 0) {
            nextPlayer()
        }

        calculateStandings()
    }

    private fun calculateStandings() {
        val list = players.value!!
        // sort players in ascending order by their points left to zero
        list.sortBy { item -> item.pointsLeft }

        for (index in 1..list.size) {
            list[index - 1].currentPosition = index
        }

        players.value = list
    }

    fun missed() {
        addPoints(Field(0, 0))
    }
}

class GameMainViewViewModelFactory(private val repository: PlayerRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameMainViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameMainViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}