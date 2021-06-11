package de.fbirk.doubleout.model.Match

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Match::class], version = 1)
abstract class MatchDatabase : RoomDatabase(){
    abstract fun matchDao(): MatchDao
}