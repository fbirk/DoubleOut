package de.fbirk.doubleout.model

import android.content.res.Resources
import de.fbirk.doubleout.R

data class Field(
    val value: Int,
    val factor: Int,
    val pos: FieldPosition = FieldPosition.NONE
) {
    override fun toString(): String {
        return super.toString()
        //if (pos != FieldPosition.NONE) {
          //  val posText = if (pos == FieldPosition.LOWER) Resources.getSystem()
            //    .getString(R.string.field_lower) else Resources.getSystem()
              //  .getString(R.string.field_upper)
           //  return factorToString() + " " + posText + " " + value
        //}
        // return factorToString() + " " + value
    }

    private fun factorToString(): String {
        return when (factor) {
            2 -> Resources.getSystem().getString(R.string.field_double)
            3 -> Resources.getSystem().getString(R.string.field_tripple)
            else -> Resources.getSystem().getString(R.string.field_single)
        }
    }
}

enum class FieldPosition {
    LOWER,
    UPPER,
    NONE
}