package de.fbirk.doubleout.model.Player

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey val number: Int,
    val lostGames: Int,
    val wonGames: Int,
    val avgPoints: Double,
    val name: String
)