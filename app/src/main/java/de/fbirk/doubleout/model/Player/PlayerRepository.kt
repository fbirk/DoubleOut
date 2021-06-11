package de.fbirk.doubleout.model.Player

import kotlinx.coroutines.flow.Flow

class PlayerRepository constructor(private val playerDao: PlayerDao) {

    fun getPlayerById(id: Int): Flow<Player> {
        return playerDao.loadById(id)
    }

    fun getPlayerByName(name: String): Flow<Player> {
        return playerDao.loadByName(name)
    }

    fun getAllPlayers(): Flow<List<Player>> {
        return playerDao.loadAllPlayers()
    }
}