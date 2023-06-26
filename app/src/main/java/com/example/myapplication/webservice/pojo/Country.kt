package com.example.myapplication.webservice.pojo

import com.example.myapplication.webservice.pojo.info.*

data class Country(
    var name: Name,
    val altSpellings: List<String>?,
    val area: Float,
    val borders: List<String>?,
    val capital: List<String>?,
    val capitalInfo: CapitalInfo?,
    val car: Car?,
    val coatOfArms: CoatOfArms?,
    val continents: List<String>,
    val demonyms: Demonyms?,
    val fifa: String?,
    val flag: String?,
    val flags: Flags?,
    val independent: Boolean,
    val landlocked: Boolean,
    val latlng: List<Double>?,
    val population: Int,
    val region: String?,
    val startOfWeek: String?,
    val status: String?,
    val subregion: String?,
    val timezones: List<String>?,
    val tld: List<String>?,
    val unMember: Boolean,
    var languages: Map<String, String>?,
    var maps: Map<String, String>?,
    var currencies: Map<String, Currency>?,
    var translations: Map<String, Translation> = emptyMap()
)