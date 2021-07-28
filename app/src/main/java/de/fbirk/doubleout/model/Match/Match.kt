package de.fbirk.doubleout.model.Match

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.model.MatchSet
import de.fbirk.doubleout.model.TimeConverter.TimeConverter
import java.time.LocalDate
import java.util.*

@Entity
@TypeConverters(TimeConverter::class)
data class Match(
    @PrimaryKey(autoGenerate = true) val number: Int,
    val date: Date = Calendar.getInstance().time,
)
