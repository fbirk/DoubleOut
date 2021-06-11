package de.fbirk.doubleout.model.Player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    fun save(player: Player)

    @Insert
    fun save(players: List<Player>)

    @Query("SELECT * FROM 'Player' WHERE number = :playerId")
    fun loadById(playerId: Int): Flow<Player>

    @Query("SELECT * From 'Player' WHERE name = :name")
    fun loadByName(name: String): Flow<Player>

    @Query("SELECT * FROM 'Player'")
    fun loadAllPlayers():Flow<List<Player>>
}
