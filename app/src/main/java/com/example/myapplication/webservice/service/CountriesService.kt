package com.example.myapplication.webservice.service

import com.example.myapplication.webservice.pojo.Country
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CountriesService {
    @GET("all")
    suspend fun getAll(): List<Country>

    @GET
    @Streaming
    suspend fun downloadImage(@Url imageUrl: String): Response<ResponseBody?>
}