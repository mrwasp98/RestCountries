package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.CountryInfoDao
import com.example.myapplication.data.model.CountryInfo
import com.example.myapplication.webservice.RetrofitInstance
import com.example.myapplication.webservice.pojo.Country

class CountriesRepository(private val countryInfoDao: CountryInfoDao) {
    private val countriesService = RetrofitInstance.creditCardService
    suspend fun getCountries(): List<CountryInfo> {
        val result: MutableList<CountryInfo> = mutableListOf()
        val countriesFromDB = countryInfoDao.getAllCountries()
        if(countriesFromDB.isEmpty()) {
            val countriesFromAPI = countriesService.getAll()
            if(countriesFromAPI.isNotEmpty()){
                countriesFromAPI.forEach { country: Country ->
                    val languages = country.languages ?: emptyMap() //antarctic has no languages
                    val currencies = country.currencies?.map { c -> c.value.name } ?: listOf()
                    result.add(
                        CountryInfo(
                            commonName = country.name.common,
                            officialName = country.name.official,
                            continent = country.continents.first(),
                            languages = languages.values.toList(),
                            flag = null,
                            unMember = country.unMember,
                            independent = country.independent,
                            capitals = country.capital,
                            population = country.population,
                            area = country.area,
                            latlng = country.latlng,
                            timezones = country.timezones,
                            carSide = country.car?.side ?: "carSide",
                            currencies = currencies,
                            coatOfArms = null,
                            flagURL = country.flags?.png,
                            coatOfArmsURL = country.coatOfArms?.png
                        )
                    )
                }
                //persist to DB, not present
                countryInfoDao.insert(result)
            }
        } else {
            result.addAll(countriesFromDB)
        }
        return result
    }

    suspend fun getFlagImage(countryInfo: CountryInfo, url: String): ByteArray? {
        var result: ByteArray? = null
        val countryFromDB = countryInfoDao.getCountry(countryInfo.commonName)
        if(countryFromDB.flag == null) {
            result = getImageFromAPI(url)
            countryFromDB.flag = result
            countryInfoDao.update(countryFromDB)
        } else {
            result = countryFromDB.flag
        }
        return result
    }

    suspend fun getCoatOfArmsImage(countryInfo: CountryInfo, url: String): ByteArray? {
        var result: ByteArray? = null
        val countryFromDB = countryInfoDao.getCountry(countryInfo.commonName)
        if(countryFromDB.coatOfArms == null) {
            result = getImageFromAPI(url)
            countryFromDB.coatOfArms = result
            countryInfoDao.update(countryFromDB)
        } else {
            result = countryFromDB.coatOfArms
        }
        return result
    }

    private suspend fun getImageFromAPI(url: String): ByteArray? {
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