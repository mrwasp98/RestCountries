package com.example.myapplication.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return gson.fromJson(value, Array<String>::class.java)?.toList()
    }

    @TypeConverter
    fun fromLatLngList(value: List<Double>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLatLngList(value: String?): List<Double>? {
        val type = object : TypeToken<List<Double>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMutableStateByteArray(value: MutableState<ByteArray?>): ByteArray? {
        return value.value
    }

    @TypeConverter
    fun toMutableStateByteArray(value: ByteArray?): MutableState<ByteArray?> {
        return mutableStateOf(value)
    }
}