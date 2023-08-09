package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.CountryInfoDao
import com.example.myapplication.data.model.CountryInfo

@Database(
    entities = [CountryInfo::class],
    version = 1,
    exportSchema = false
)

abstract class CountryInfoDatabase : RoomDatabase() {
    abstract fun countryInfoDao(): CountryInfoDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: CountryInfoDatabase? = null

        fun getInstance(context: Context): CountryInfoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): CountryInfoDatabase {
            return Room.databaseBuilder(context, CountryInfoDatabase::class.java, "country_info_database")
                .build()
        }
    }
}

