package com.example.myapplication.ui.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CountryInfo
import com.example.myapplication.data.model.CountryInfoView
import com.example.myapplication.data.repository.CountriesRepository
import com.example.myapplication.webservice.pojo.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountriesViewModel constructor(
    private val repository: CountriesRepository
) : ViewModel() {
    private var _countries: List<CountryInfo> = listOf()
//TODO LISTEN WHEN CONNECTION

    //view state
    var countriesInfoView: MutableState<List<CountryInfoView>> = mutableStateOf(listOf())
    val retrievingDataProgressState = mutableStateOf(0f)
    val isFlagsFetchCompleted =
        mutableStateOf(false) //necessary to signal to the view that fetching is completed
    private val isCountriesFetchCompleted =
        mutableStateOf(false) //necessary to display LinearProgressIndicator state

    //continent filter
    private val defContinentFilterContent = "Filter by Continent"
    val continentFilterContent = mutableStateOf(defContinentFilterContent)
    val continentFilterContentIsActive = mutableStateOf(false)

    //language filter
    private var languageFilterContentIsActive = false
    var languageFilterContent = mutableStateOf("")

    private var alreadyResolvingQuery = false
    /**
     * The aim of this function is to retrieve all countries from the server. From these will be
     * built the view state, digested from vm in a suitable way for the latter.
     **/
    fun fetchCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countriesRetrieved = repository.getCountries()
                this@CountriesViewModel._countries =
                    countriesRetrieved.sortedBy { country -> country.commonName }
                isCountriesFetchCompleted.value = true
                retrievingDataProgressState.value = 0.5f
                //retrieve flags for each country
                fetchFlags()
                //create view data
                countriesInfoView.value = getCountriesTransformedForView()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun getCountriesTransformedForView(list: List<CountryInfo>? = null): List<CountryInfoView> {
        val listToUse = list ?: _countries
        return listToUse.map { country: CountryInfo ->
            val flagImageBitmap = if(country.flag != null) BitmapFactory.decodeByteArray(country.flag!!, 0, country.flag!!.size) else null
            val coatOfArmsBitmap = if(country.coatOfArms != null) BitmapFactory.decodeByteArray(country.coatOfArms!!, 0, country.coatOfArms!!.size) else null
            CountryInfoView(
                commonName = country.commonName,
                officialName = country.officialName,
                continent = country.continent,
                languages = country.languages,
                flag = flagImageBitmap,
                unMember = country.unMember,
                independent = country.independent,
                capitals = country.capitals,
                population = country.population,
                area = country.area,
                latlng = country.latlng,
                timezones = country.timezones,
                carSide = country.carSide,
                currencies = country.currencies,
                coatOfArms = mutableStateOf(coatOfArmsBitmap),
            )
        }
    }

    /**
     * The aim of this function is to retrieve all countries flags from the server. Retrieved data will be
     * added to the view state, and the end of processing signaled to the view (value of state isFlagsFetchCompleted = true).
     **/
    private suspend fun fetchFlags() {
        try {
            var flagsRetrieved = 0
            _countries
                .filter { country ->
                    if (country.flagURL != null && country.flag == null) true
                    else {
                        retrievingDataProgressState.value += 0.002f
                        false
                    }
                }
                .forEach { country: CountryInfo ->
                    val flagImageBytes = repository.getFlagImage(country, country.flagURL!!)
                    flagsRetrieved += 1
                    country.flag = flagImageBytes
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
        if(!alreadyResolvingQuery) {
            viewModelScope.launch(Dispatchers.IO) {
                alreadyResolvingQuery = true
                try {
                    val countrySel =
                        _countries.first { country -> country.commonName == commonName && country.coatOfArms == null }
                    if (countrySel.coatOfArmsURL != null) {
                        val flagImageBytes =
                            repository.getCoatOfArmsImage(countrySel, countrySel.coatOfArmsURL!!)
                        countrySel.coatOfArms = flagImageBytes
                        //update view
                        val coatOfArmsBitmap =
                            if (flagImageBytes != null) BitmapFactory.decodeByteArray(
                                flagImageBytes,
                                0,
                                flagImageBytes.size
                            ) else null
                        countriesInfoView.value.first { country -> country.commonName == countrySel.commonName }.coatOfArms.value =
                            coatOfArmsBitmap
                    }
                } catch (e: Exception) {
                    // Handle error
                }
                alreadyResolvingQuery = false
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
            } else countriesInfoView.value = getCountriesTransformedForView()
        } else {
            languageFilterContentIsActive = true
            val filteredList = _countries.filter { c ->
                c.languages.any { lan -> lan.lowercase().contains(language.lowercase()) }
            }.filter { c ->
                //filter again wrt the continent if necessary
                if (continentFilterContentIsActive.value) {
                    //if here, the continent filter is active
                    //we need to filter more...
                    c.continent == continentFilterContent.value
                } else true
            }
            countriesInfoView.value = getCountriesTransformedForView(filteredList)
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
            if (languageFilterContentIsActive)
                filterCountriesByLanguage(languageFilterContent.value)
            else countriesInfoView.value = getCountriesTransformedForView()
        } else {
            continentFilterContent.value = continent
            continentFilterContentIsActive.value = true
            val filteredList = _countries.filter { c ->
                c.continent == continent
            }.filter { c ->
                //filter again wrt the language if necessary
                if (languageFilterContentIsActive) {
                    //if here, the language filter is active
                    //we need to filter more...
                    c.languages.any { lan ->
                        lan.lowercase().contains(languageFilterContent.value.lowercase())
                    }
                } else true
            }
            countriesInfoView.value = getCountriesTransformedForView(filteredList)
        }
    }
}

class ViewModelFactory(private val repository: CountriesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}