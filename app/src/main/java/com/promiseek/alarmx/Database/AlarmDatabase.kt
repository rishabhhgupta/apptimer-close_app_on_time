package com.promiseek.alarmx.Database

import android.content.Context
import androidx.room.*

@Database(entities = [AlarmsPogo::class], version = 1)
@TypeConverters(Converter::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao():AlarmDao

    companion object{
        @Volatile
        private var INSTANCE: AlarmDatabase?=null

        fun getDatabase(context:Context):AlarmDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext
                    ,AlarmDatabase::class.java,"alarmdb")
                        .build()
                }
            }
            return INSTANCE!!

        }
    }


}