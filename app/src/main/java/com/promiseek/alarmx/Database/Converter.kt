package com.promiseek.alarmx.Database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.sql.Time

class Converter {
    @TypeConverter
    fun stringToArrayList(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun arrayListToString(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }



}