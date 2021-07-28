package de.fbirk.doubleout.model.Player

import androidx.lifecycle.LiveData

class PlayerRepository(private val playerDao: PlayerDao) {

    fun getPlayerById(id: Int): LiveData<Player> {
        return playerDao.loadById(id)
    }

    fun getPlayerByName(name: String): LiveData<Player> {
        return playerDao.loadByName(name)
    }

    fun getAllPlayers(): LiveData<List<Player>> {
        return playerDao.loadAllPlayers()
    }

    fun getAllPlayersWithId(players: List<Int>): LiveData<List<Player>> {
        return playerDao.loadAllSelectedPlayers(players)
    }

    fun savePlayer(player: Player) {
        playerDao.save(player)
    }

    fun savePlayers(players: List<Player>) {
        playerDao.save(players)
    }

    fun updatePlayer(player: Player) {
        playerDao.update(player)
    }

}