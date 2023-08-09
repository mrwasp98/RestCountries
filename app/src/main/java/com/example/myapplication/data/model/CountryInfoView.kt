package com.example.myapplication.data.model

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState

data class CountryInfoView(
    var commonName: String,
    var officialName: String,
    var languages: List<String>,
    val unMember: Boolean,
    val independent: Boolean,
    val capitals: List<String>?,
    val population: Int,
    val area: Float,
    val latlng: List<Double>?,
    var continent: String,
    val timezones: List<String>?,
    val carSide: String?,
    val currencies: List<String> = listOf(),
    var flag: Bitmap?,
    var flagURL: String?,
    var coatOfArms: MutableState<Bitmap?>,
    var coatOfArmsURL: String?,
) {
    companion object {
        private val propertyNamesMap = mapOf(
            "commonName" to "Common Name",
            "officialName" to "Official Name",
            "nativeName" to "Native Name",
            "languages" to "Languages",
            "unMember" to "UNESCO Member",
            "independent" to "Independent",
            "capitals" to "Capitals",
            "population" to "Population",
            "area" to "Area",
            "latlng" to "Latitude/Longitude",
            "continent" to "Continent",
            "timezones" to "Timezones",
            "carSide" to "Car Side",
            "currencies" to "Currencies",
        )

        private val propertyPosition = listOf(
            "commonName",
            "officialName",
            "nativeName",
            "unMember",
            "independent",
            "capitals",
            "population",
            "languages",
            "area",
            "latlng",
            "continent",
            "timezones",
            "carSide",
            "currencies"
        )

        fun getBeautifiedPropertyName(propertyName: String): String {
            return propertyNamesMap[propertyName] ?: propertyName.capitalize()
        }

        fun getPropertyPosition(propertyName: String): Int {
            return propertyPosition.indexOf(propertyName)
        }

        fun getPropertyValueAsString(propertyName: String, instance: CountryInfoView): String {
            val propertyValue = instance::class.members.find { it.name == propertyName }?.call(instance)
            return when (propertyValue) {
                is List<*> -> propertyValue.joinToString(", ")
                else -> propertyValue?.toString() ?: "emptyString"
            }
        }
    }
}