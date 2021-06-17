package de.fbirk.doubleout.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.fbirk.doubleout.model.Match.MatchRepository
import de.fbirk.doubleout.model.Player.Player

class GameStartViewModel : ViewModel() {
    // private val repo: MatchRepository = MatchRepository()
    var selectedPlayers = MutableLiveData<ArrayList<Player>>()
    var allPlayers: MutableLiveData<ArrayList<Player>> = MutableLiveData(
        arrayListOf<Player>(
            Player(1, 0, 0, 0.0, "Player 1"),
            Player(2, 0, 0, 0.0, "Player2"),
            Player(3, 0, 0, 0.0, "Player3")
        )
    )

    fun addPlayer(player: Player) {
        if (selectedPlayers.value != null && selectedPlayers.value?.contains(player) == false) {
            var currentPlayers = selectedPlayers.value
            if (currentPlayers != null) {
                currentPlayers.add(player)
                selectedPlayers.postValue(currentPlayers!!)
            }
        } else {
            var playerList = arrayListOf<Player>(player)
            selectedPlayers.postValue(playerList)
        }
    }
}