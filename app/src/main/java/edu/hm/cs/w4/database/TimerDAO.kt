package edu.hm.cs.w4.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.hm.cs.w4.Timer

@Dao
interface TimerDAO {
    @Insert
    fun insert(timers: Timer)

    @Update
    fun update(timers: Timer)

    @Delete
    fun delete(timers: Timer)

    @Query("SELECT * FROM time_table ORDER BY length_of_timer ASC")
    fun getAllTimers() : LiveData<List<Timer>>
}