package de.fbirk.doubleout.model.Match

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MatchDao {
    @Insert
    fun save(match: Match)

    @Query("SELECT * FROM 'Match' WHERE number = :matchId")
    fun loadById(matchId: Int): LiveData<Match>

    //@Query("SELECT * FROM 'Match' WHERE date = :date")
    //fun loadByDate(date: Date): LiveData<List<Match>>
}