package de.fbirk.doubleout.model

import de.fbirk.doubleout.model.Player.Player

data class Turn(
    val score: Field,
    val player: Player,
    val matchSetReference: Int
)
