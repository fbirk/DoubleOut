package de.fbirk.doubleout.model.Match

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.Set
import java.util.*

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true) val number: Int,
    val date: Date,
    val players: List<Player>,
    val sets: List<Set>
)
