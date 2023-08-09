package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.CountryInfoDatabase
import com.example.myapplication.data.repository.CountriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application(){
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CountryInfoDatabase.getInstance(this) }
    val repository by lazy { CountriesRepository(database.countryInfoDao()) }
}
