package de.fbirk.doubleout.model

import android.content.res.Resources
import de.fbirk.doubleout.R

data class Field(
    val value: Int,
    val factor: Int,
    val pos: FieldPosition = FieldPosition.NONE
) {
    override fun toString(): String {
        return factorToString() + "" + value
    }

    private fun factorToString(): String {
        return when (factor) {
            2 -> "D"
            3 -> "T"
            else -> "S"
        }
    }
}

enum class FieldPosition {
    LOWER,
    UPPER,
    NONE
}