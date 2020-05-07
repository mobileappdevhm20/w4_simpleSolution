package edu.hm.cs.w4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.hm.cs.w4.Timer

@Database(entities = [Timer::class],
    version = 1,
    exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {

    abstract val timerDAO: TimerDAO

    companion object {
        @Volatile
        private var INSTANCE: TimerDatabase? = null

        fun getInstance(context: Context): TimerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimerDatabase::class.java,
                        "timer_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}