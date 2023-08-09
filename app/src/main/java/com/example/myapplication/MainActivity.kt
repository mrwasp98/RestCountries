package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.view.ContinentsScreen
import com.example.myapplication.ui.view.CountriesScreen
import com.example.myapplication.ui.view.CountryScreen
import com.example.myapplication.ui.viewmodel.CountriesViewModel
import com.example.myapplication.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel : CountriesViewModel by viewModels {
            ViewModelFactory((application as BaseApplication).repository)
        }
        viewModel.fetchCountries()
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SetupNavigation(viewModel: CountriesViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.CountriesScreen.route) {
        composable(Screen.CountriesScreen.route) {
            CountriesScreen(navController, viewModel)
        }
        composable(Screen.ContinentsScreen.route) {
            ContinentsScreen(navController, viewModel)
        }
        composable(
            Screen.CountryScreen.route,
            arguments = listOf(navArgument("countryId") { type = NavType.StringType })
        ) { backStackEntry ->
            CountryScreen(navController, backStackEntry.arguments?.getString("countryId"),viewModel)
        }
    }
}


