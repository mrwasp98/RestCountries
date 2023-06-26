package com.example.myapplication.repository

import com.example.myapplication.webservice.RetrofitInstance
import com.example.myapplication.webservice.pojo.Country

class CountriesRepository {
    private val countriesService = RetrofitInstance.creditCardService

    suspend fun getCountries(): List<Country> {
        return countriesService.getAll()
    }

    suspend fun getFlagImage(url: String): ByteArray? {
        return try {
            val response = countriesService.downloadImage(url)
            if (response.isSuccessful) {
                response.body()?.bytes()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}