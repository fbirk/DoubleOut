package de.fbirk.doubleout.model.Player

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1)
abstract class PlayerDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}