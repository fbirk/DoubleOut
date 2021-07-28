package de.fbirk.doubleout.model.Player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Number")
    val number: Int = 0,

    @ColumnInfo(name = "MatchReferenceId")
    val matchReference: Int = 0,

    @ColumnInfo(name = "LostGames")
    val lostGames: Int = 0,

    @ColumnInfo(name = "WonGames")
    val wonGames: Int = 0,

    @ColumnInfo(name = "AvgPoints")
    val avgPoints: Double = 0.0,

    @ColumnInfo(name = "Name")
    val name: String = ""
) {
    var currentThrows: Int = 0
    var currentPosition: Int = 0
    var pointsLeft: Int = 0
    var currentAvg: Double = 0.0
}