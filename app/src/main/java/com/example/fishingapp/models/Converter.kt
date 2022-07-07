package com.example.fishingapp.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object Converter {
    @TypeConverter
    fun restoreBasesAndCondiciones(listOfString: String): List<BaseOrCondicion> {
        return Gson().fromJson(listOfString,object : TypeToken<List<BaseOrCondicion>>(){}.type)
    }

    @TypeConverter
    fun saveBasesAndCondicionesOfString(listOfString: List<BaseOrCondicion>): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun restoreRanking(listOfString: String): List<Reporte> {
        return Gson().fromJson(listOfString,object : TypeToken<List<Reporte>>(){}.type)
    }

    @TypeConverter
    fun saveRankingOfString(listOfString: List<Reporte>): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        return Gson().fromJson(value, object: TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}