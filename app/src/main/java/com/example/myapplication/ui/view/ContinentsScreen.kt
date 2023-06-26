package com.example.myapplication.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Screen
import com.example.myapplication.ui.viewmodel.CountriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContinentsScreen(navController: NavController, viewModel: CountriesViewModel) {
    val continents =
        listOf("Europe", "Asia", "Oceania", "North America", "South America", "Antarctica")
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Choose a Continent")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    itemsIndexed(continents) { _, continent ->
                        Button(
                            onClick = {
                                viewModel.filterCountriesByContinent(continent)
                                navController.navigate(
                                    Screen.CountriesScreen.route
                                )
                            },
                            modifier = Modifier
                                .size(130.dp)
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = 10.dp,
                                    end = 10.dp,
                                )
                                .clip(CircleShape), // Imposta la forma del pulsante a cerchio
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            content = {
                                Box {
                                    /*Image(
                                    painter = rememberDrawablePainter(getContinentImage(continent)),
                                    contentDescription = continent,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center)
                                )*/
                                    Text(
                                        text = continent,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

