package com.example.myapplication.ui.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.CountryInfoView
import com.example.myapplication.repository.CountriesRepository
import com.example.myapplication.webservice.pojo.Country
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {
    private val repository = CountriesRepository()

    private var _countriesRetrieved: List<Country> = listOf()
    private var _countriesTransformed: List<CountryInfoView> = listOf()

    //view state
    var countriesInfoView: MutableState<List<CountryInfoView>> = mutableStateOf(listOf())
    val retrievingDataProgressState = mutableStateOf(0f)
    val isFlagsFetchCompleted = mutableStateOf(false) //necessary to signal to the view that fetching is completed
    private val isCountriesFetchCompleted = mutableStateOf(false) //necessary to display LinearProgressIndicator state

    //continent filter
    private val defContinentFilterContent = "Filter by Continent"
    val continentFilterContent = mutableStateOf(defContinentFilterContent)
    val continentFilterContentIsActive = mutableStateOf(false)

    //language filter
    private var languageFilterContentIsActive = false
    var languageFilterContent = mutableStateOf("")

    /**
     * The aim of this function is to retrieve all countries from the server. From these will be
     * built the view state, digested from vm in a suitable way for the latter.
     **/
    fun fetchCountries() {
        viewModelScope.launch {
            try {
                val countriesRetrieved = repository.getCountries()
                this@CountriesViewModel._countriesRetrieved =
                    countriesRetrieved.sortedBy { country -> country.name.common }
                //create data for the view
                val countriesDigestedForTheView = mutableListOf<CountryInfoView>()
                _countriesRetrieved
                    .forEach { country: Country ->
                        val languages = country.languages ?: emptyMap() //antarctic has no languages
                        val currencies = country.currencies?.map { c -> c.value.name } ?: listOf()
                        countriesDigestedForTheView.add(
                            CountryInfoView(
                                commonName = country.name.common,
                                officialName = country.name.official,
                                continent = country.continents.first(),
                                languages = languages.values.toList(),
                                flag = mutableStateOf(null),
                                unMember = country.unMember,
                                independent = country.independent,
                                capitals = country.capital,
                                population = country.population,
                                area = country.area,
                                latlng = country.latlng,
                                timezones = country.timezones,
                                carSide = country.car?.side ?: "carSide",
                                currencies = currencies,
                                coatOfArms = mutableStateOf(null)
                            )
                        )
                    }
                _countriesTransformed = countriesDigestedForTheView
                countriesInfoView.value = _countriesTransformed.toList()
                isCountriesFetchCompleted.value = true
                retrievingDataProgressState.value = 0.5f
                //retrieve now flags
                fetchFlags()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * The aim of this function is to retrieve all countries flags from the server. Retrieved data will be
     * added to the view state, and the end of processing signaled to the view (value of state isFlagsFetchCompleted = true).
     **/
    private suspend fun fetchFlags() {
        try {
            var flagsRetrieved = 0
            _countriesRetrieved
                .filter { country -> country.flags?.png != null }
                .forEach { country: Country ->
                    val flagImageBytes = repository.getFlagImage(country.flags!!.png)
                    flagsRetrieved += 1
                    //let convert ByteArray to Bitmap
                    val flagImage =
                        BitmapFactory.decodeByteArray(flagImageBytes, 0, flagImageBytes!!.size)
                    _countriesTransformed.firstOrNull { c -> c.commonName == country.name.common }?.flag?.value =
                        flagImage
                    retrievingDataProgressState.value += 0.002f
                }
            isFlagsFetchCompleted.value = true
        } catch (e: Exception) {
            // Handle error
        }
    }

    /**
     * The aim of this function is to retrieve the coat of arms image of a given country
     * from the server. Retrieved data will be added to the view state of the specified country.
     * @param commonName the common name of the country whose coat of arms image is requested.
     **/
    fun fetchCoatOfArms(commonName: String) {
        viewModelScope.launch {
            try {
                val country = _countriesRetrieved
                    .first { country -> country.name.common == commonName }
                if (country.coatOfArms?.png != null) {
                    val flagImageBytes = repository.getFlagImage(country.coatOfArms.png)
                    //let convert ByteArray to Bitmap
                    val flagImage =
                        BitmapFactory.decodeByteArray(flagImageBytes, 0, flagImageBytes!!.size)
                    _countriesTransformed.first { c -> c.commonName == commonName }.coatOfArms.value =
                        flagImage
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    /**
     * The aim of this function is to filter countries view data (countriesInfoView) with respect
     * to the language selected by the user.
     * @param language the language that is spoken in the searched countries.
     **/
    fun filterCountriesByLanguage(language: String) {
        if (language.isEmpty()) {
            languageFilterContentIsActive = false
            //if continent filter is working, filter its result
            //...else restore original data (_countriesTransformed)
            if (continentFilterContentIsActive.value) {
                filterCountriesByContinent(continentFilterContent.value)
            } else countriesInfoView.value = _countriesTransformed.toList()
        } else {
            languageFilterContentIsActive = true
            val filteredList = _countriesTransformed.filter { c ->
                c.languages.any { lan -> lan.lowercase().contains(language.lowercase()) }
            }.filter { c ->
                //filter again wrt the continent if necessary
                if (continentFilterContentIsActive.value) {
                    //if here, the continent filter is active
                    //we need to filter more...
                    c.continent == continentFilterContent.value
                } else true
            }
            countriesInfoView.value = filteredList
        }
    }

    /**
     * The aim of this function is to filter countries view data (countriesInfoView) with respect
     * to the continent selected by the user.
     * @param continent the continent where are located searched countries.
     **/
    fun filterCountriesByContinent(continent: String) {
        if (continent.isEmpty()) {
            //if language filter is working, filter its result
            //...else restore original data (_countriesTransformed)
            continentFilterContentIsActive.value = false
            continentFilterContent.value = defContinentFilterContent
            if(languageFilterContentIsActive)
                filterCountriesByLanguage(languageFilterContent.value)
            else countriesInfoView.value = _countriesTransformed.toList()
        } else {
            continentFilterContent.value = continent
            continentFilterContentIsActive.value = true
            val filteredList = _countriesTransformed.filter { c ->
                c.continent == continent
            }.filter { c ->
                //filter again wrt the language if necessary
                if (languageFilterContentIsActive) {
                    //if here, the language filter is active
                    //we need to filter more...ù
                    c.languages.any { lan ->
                        lan.lowercase().contains(languageFilterContent.value.lowercase())
                    }
                } else true
            }
            countriesInfoView.value = filteredList
        }
    }
}