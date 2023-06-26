package com.example.myapplication.webservice

import com.example.myapplication.webservice.service.CountriesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val COUNTRIES_BASE_URL = "https://restcountries.com/v3.1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(COUNTRIES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val creditCardService: CountriesService by lazy {
        retrofit.create(CountriesService::class.java)
    }
}