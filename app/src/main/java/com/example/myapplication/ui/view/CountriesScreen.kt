package com.example.myapplication.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Screen
import com.example.myapplication.ui.view.composables.AutoCompleteTextView
import com.example.myapplication.ui.view.composables.CountryCard
import com.example.myapplication.ui.view.composables.Loader
import com.example.myapplication.ui.viewmodel.CountriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesScreen(navController: NavController, viewModel: CountriesViewModel) {
    val isSuggestionMenuOpen = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "World Countries")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (!viewModel.isFlagsFetchCompleted.value)
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Loader(modifier = Modifier.height(360.dp))
                        LinearProgressIndicator(
                            progress = viewModel.retrievingDataProgressState.value,
                            modifier = Modifier.fillMaxWidth()
                        )
                        val label = if(viewModel.retrievingDataProgressState.value < 0.5f) "Loading countries..." else "Loading flags..."
                        Text(
                            text = label,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                else {
                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.ContinentsScreen.route
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .padding(
                                top = 8.dp,
                                bottom = 4.dp,
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(
                            width = if (viewModel.continentFilterContentIsActive.value) 2.dp else 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = RoundedCornerShape(4.dp),
                        content = {
                            Text(
                                text = viewModel.continentFilterContent.value,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                            if (viewModel.continentFilterContentIsActive.value) {
                                IconButton(onClick = { viewModel.filterCountriesByContinent("") }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )

                    AutoCompleteTextView(
                        modifier = Modifier
                            .padding(
                                bottom = 8.dp,
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        query = viewModel.languageFilterContent.value,
                        queryLabel = "Filter by Language",
                        onQueryChanged = { language ->
                            //call the view model method to update addressPlaceItemPredictions
                            viewModel.filterCountriesByLanguage(language)
                            isSuggestionMenuOpen.value = true
                            viewModel.languageFilterContent.value = language
                        },
                        predictions = viewModel.countriesInfoView.value
                            .flatMap { c -> c.languages }
                            .filter { l ->
                                l.lowercase()
                                    .contains(viewModel.languageFilterContent.value.lowercase())
                            }.distinct(),
                        onClearClick = {
                            //call the view model method to clear the predictions
                            viewModel.filterCountriesByLanguage("")
                            viewModel.languageFilterContent.value = ""
                            isSuggestionMenuOpen.value = false
                        },
                        onDoneActionClick = {
                            isSuggestionMenuOpen.value = false
                        },
                        onItemClick = {
                            viewModel.filterCountriesByLanguage(it)
                            viewModel.languageFilterContent.value = it
                            isSuggestionMenuOpen.value = false
                        },
                        isSuggestionMenuOpen = isSuggestionMenuOpen.value
                    ) {
                        //Define how the items need to be displayed
                        Text(it, fontSize = 16.sp)
                    }

                    if (viewModel.countriesInfoView.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.padding(
                                end = 8.dp,
                                start = 8.dp
                            )
                        ) {
                            itemsIndexed(viewModel.countriesInfoView.value) { _, country ->
                                CountryCard(
                                    onClick = {
                                        viewModel.fetchCoatOfArms(country.commonName)
                                        navController.navigate(
                                            Screen.CountryScreen.createRoute(
                                                country.commonName
                                            )
                                        )
                                    },
                                    country = country
                                )
                            }
                        }
                    } else {
                        ElevatedAssistChip(
                            enabled = false,
                            onClick = { },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            shape = CircleShape,
                            label = { Text(text = "No results matching") },
                        )
                    }
                }
            }
        }
    )
}
