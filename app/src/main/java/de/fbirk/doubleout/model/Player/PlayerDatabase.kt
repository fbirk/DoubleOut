package de.fbirk.doubleout.model.Player

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import de.fbirk.doubleout.model.TimeConverter.TimeConverter
import de.fbirk.doubleout.ui.game.start.GameStartAddPlayerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Player::class], version = 1, exportSchema = false)
@TypeConverters(TimeConverter::class)
abstract class PlayerDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: PlayerDatabase? = null

        fun getInstance(context: Context): PlayerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlayerDatabase::class.java,
                        "Player"
                    )
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private class PlayerDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { playerDatabase ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(playerDatabase.playerDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(playerDao: PlayerDao) {
            var default = Player(0, 0, 0, 0, 0.0, "Player 1")
            playerDao.save(default)
        }
    }
}