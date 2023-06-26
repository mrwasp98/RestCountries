package com.example.myapplication

sealed class Screen(val route: String) {
    object CountriesScreen : Screen("countries")
    object ContinentsScreen : Screen("continents")
    object CountryScreen : Screen("country/{countryId}") {
        fun createRoute(countryId: String): String = "country/$countryId"
    }
}