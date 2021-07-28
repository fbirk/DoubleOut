package de.fbirk.doubleout.model.Player

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    fun save(player: Player)

    @Update
    fun update(player: Player)

    @Insert
    fun save(players: List<Player>)

    @Query("SELECT * FROM 'Player' WHERE Number = :playerId")
    fun loadById(playerId: Int): LiveData<Player>

    @Query("SELECT * From 'Player' WHERE Name = :name")
    fun loadByName(name: String): LiveData<Player>

    @Query("SELECT * FROM 'Player'")
    fun loadAllPlayers(): LiveData<List<Player>>

    @Query("SELECT * FROM 'Player' WHERE Number IN (:playerIds)")
    fun loadAllSelectedPlayers(playerIds: List<Int>): LiveData<List<Player>>
}
