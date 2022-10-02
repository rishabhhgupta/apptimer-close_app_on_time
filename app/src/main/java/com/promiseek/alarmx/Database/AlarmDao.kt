package com.promiseek.alarmx.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {
    @Insert
    fun insert(alarm:AlarmsPogo)

    @Update
    fun update(alarm:AlarmsPogo)

    @Delete
    fun delete(alarm:AlarmsPogo)

    @Query("Select * from alarms")
    fun getAlarms(): LiveData<List<AlarmsPogo>>

    @Query("Select  id from alarms order by id DESC LIMIT 1")
    fun getLastId():Long

}