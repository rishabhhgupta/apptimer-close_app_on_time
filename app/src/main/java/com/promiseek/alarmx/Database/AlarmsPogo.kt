package com.promiseek.alarmx.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "alarms")
data class AlarmsPogo(
    @PrimaryKey(autoGenerate = true)
    val id:Long, val time: String, val dayChosen:ArrayList <String>, val packageNames:ArrayList<String>,
    var onOrOf:Boolean
)
