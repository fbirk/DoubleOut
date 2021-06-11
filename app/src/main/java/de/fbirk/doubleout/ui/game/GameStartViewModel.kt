package de.fbirk.doubleout.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.fbirk.doubleout.model.Player.Player

class GameStartViewModel : ViewModel() {
    var selectedPlayers = MutableLiveData<List<Player>>()
    var allPlayers: MutableLiveData<List<Player>> = MutableLiveData(
        listOf<Player>(
            Player(1, 0, 0, 0.0, "Player 1"),
            Player(2, 0, 0, 0.0, "Player2"),
            Player(3, 0, 0, 0.0, "Player3")
        )
    )

    fun addPlayer(player: Player) {
        if (selectedPlayers.value != null && selectedPlayers.value?.contains(player) == false) {
            selectedPlayers.value?.plus(player)
        }
    }
}