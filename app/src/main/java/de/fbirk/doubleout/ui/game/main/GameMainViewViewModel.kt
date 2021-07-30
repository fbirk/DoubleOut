package de.fbirk.doubleout.ui.game.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fbirk.doubleout.model.Field
import de.fbirk.doubleout.model.MatchSet
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Player.PlayerDatabase
import de.fbirk.doubleout.model.Player.PlayerRepository
import de.fbirk.doubleout.model.Turn

class GameMainViewViewModel(private val application: Context) : ViewModel() {
    var sets = MutableLiveData<List<MatchSet>>()
    var turns = MutableLiveData<ArrayList<Turn>>()
    var players = MutableLiveData<ArrayList<Player>>()
    var hasWinner = MutableLiveData<Boolean>()
    var currentIndex: Int = 0
    var currentThrowsInTurn = ""
    private var throwCounter: Int = 0
    private val repository: PlayerRepository

    init {
        val dao = PlayerDatabase.getInstance(application).playerDao()
        repository = PlayerRepository(dao)
        hasWinner.value = false
    }


    /**
     * initialize the game
     */
    fun initializeWithPlayers(playerNames: Array<String>): ArrayList<Player> {
        var playerList = ArrayList<Player>()

        for ((index, name) in playerNames.withIndex()) {
            val player = Player(index, 0, 0, 0, 0.0, name)
            player.pointsLeft = 301
            player.currentAvg = 0.0
            player.currentPosition = 0
            player.currentThrows = 0
            playerList.add(player)
        }

        players.postValue(playerList)
        return playerList.clone() as ArrayList<Player>
    }

    fun resetPlayers() {
        for (player in players.value!!) {
            player.pointsLeft = 301
            player.currentPosition = 0
        }
    }

    /**
     * cycle through player list and set next player index
     */
    private fun nextPlayer() {
        if (currentIndex < players.value?.size!! - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
    }

    /**
     * add points to current player, update informations
     */
    fun addPoints(field: Field) {
        var currentThrowOverviewSeparator = ""
        if (throwCounter != 0) {
            currentThrowOverviewSeparator = " |"
        } else {
            currentThrowsInTurn = ""
        }
        // generate overview of throws in this turn
        currentThrowsInTurn = if (field.value != 0) {
            currentThrowsInTurn.plus("$currentThrowOverviewSeparator $field")
        } else {
            currentThrowsInTurn.plus("$currentThrowOverviewSeparator 0")
        }

        throwCounter++

        // decrease points left for current player if not overthrown
        if (players.value!![currentIndex].pointsLeft >= (field.value * field.factor)) {
            //sets.value?.get(currentSet)?.turn?.add(Turn(field, players.value!![currentIndex]))
            players.value!![currentIndex].pointsLeft -= (field.value * field.factor)
        } else {
            // if overthrown -> skip and get next player
            throwCounter = 3
        }

        // update statistics for current player
        val currentPlayer = players.value!![currentIndex]
        players.value!![currentIndex].currentThrows++
        players.value!![currentIndex].currentAvg =
            calculateRunningMean(currentPlayer, (field.value * field.factor))


        if (throwCounter % 3 == 0) {
            throwCounter = 0
            nextPlayer()
        }

        // update leader board
        calculateStandings()
    }

    /**
     * incremental calculation of avg. points per throw
     * TODO: avg. points per turn (for 3 throws)
     */
    private fun calculateRunningMean(currentPlayer: Player, value: Int): Double {
        var avg = currentPlayer.currentAvg
        avg -= avg / currentPlayer.currentThrows
        avg += value / currentPlayer.currentThrows
        return avg
    }

    /**
     * update leaderboard and check if game is finished
     */
    private fun calculateStandings() {
        var isFinished = hasWinner.value!!
        val list = players.value!!
        // sort players in ascending order by their points left to zero
        list.sortBy { item -> item.pointsLeft }

        for (index in 1..list.size) {
            list[index - 1].currentPosition = index

            isFinished = list[index - 1].pointsLeft == 0 || isFinished
        }

        hasWinner.value = isFinished
        players.value = list
    }

    fun missed() {
        addPoints(Field(0, 0))
    }
}

class GameMainViewViewModelFactory(private val application: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameMainViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameMainViewViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}