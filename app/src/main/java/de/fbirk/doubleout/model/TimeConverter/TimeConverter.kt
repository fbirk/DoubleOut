package de.fbirk.doubleout.model.TimeConverter

import androidx.room.TypeConverter
import java.util.*

class TimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return when (value) {
            null -> Date()
            else -> Date(value)
        }
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}
