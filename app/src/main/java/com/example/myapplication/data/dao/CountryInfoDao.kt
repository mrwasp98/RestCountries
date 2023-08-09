package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.CountryInfo

@Dao
interface CountryInfoDao {

    @Query("SELECT * FROM CountryInfo")
    fun getAllCountries(): List<CountryInfo>

    @Query("SELECT * FROM CountryInfo WHERE commonName = :countryCommonName")
    fun getCountry(countryCommonName: String): CountryInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(countries: List<CountryInfo>)

    @Update
    suspend fun update(country: CountryInfo)
}