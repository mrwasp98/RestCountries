package com.example.myapplication.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.model.CountryInfoView
import com.example.myapplication.data.model.CountryInfoView.Companion.getBeautifiedPropertyName
import com.example.myapplication.data.model.CountryInfoView.Companion.getPropertyPosition
import com.example.myapplication.data.model.CountryInfoView.Companion.getPropertyValueAsString
import com.example.myapplication.ui.view.composables.CountryDetailRow
import com.example.myapplication.ui.view.composables.Loader
import com.example.myapplication.ui.view.composables.ToggleButtonRow
import com.example.myapplication.ui.viewmodel.CountriesViewModel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import kotlin.reflect.full.declaredMemberProperties

@Composable
fun CountryScreen(navController: NavController, countryId: String?, viewModel: CountriesViewModel) {
    val country = viewModel.countriesInfoView.value.first { c -> c.commonName == countryId }
    val state = rememberCollapsingToolbarScaffoldState()
    val switchingScrollValue = 0.006f
    val selectedButton = remember { mutableStateOf(0) }

    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            val textSize: TextUnit = (18 + (30 - 12) * state.toolbarState.progress).sp
            var color: Color = Color.White
            if (country.commonName == "Afghanistan") {
                //workaround needed because afghanistan has a white flag
                color =
                    if (state.toolbarState.progress < switchingScrollValue) Color.White else Color.Black
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .pin()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Image(
                bitmap = country.flag!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop, alpha = if (textSize.value == 18f) 0f else 1f
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .height(54.dp)
                    .road(
                        whenCollapsed = Alignment.TopStart,
                        whenExpanded = Alignment.TopStart
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = color
                )
            }
            Text(
                country.commonName,
                style = TextStyle(color = color, fontSize = textSize),
                modifier = Modifier
                    .road(
                        whenCollapsed = Alignment.TopStart,
                        whenExpanded = Alignment.BottomStart
                    )
                    .padding(start = 50.dp, top = 14.dp, bottom = 16.dp)
            )

        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val data = getData(country)
            item {
                ToggleButtonRow(
                    leftButtonText = "COUNTRY INFO",
                    rightButtonText = "COAT OF ARMS",
                    selectedButton = selectedButton
                )
            }
            if(selectedButton.value == 0) {
                itemsIndexed(data) { _, property ->
                    CountryDetailRow(property.first, property.second)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center, // Align content vertically at the center
                        horizontalAlignment = Alignment.CenterHorizontally // Align content horizontally at the center
                    ) {
                        if(country.coatOfArms.value != null) {
                            Image(
                                bitmap = country.coatOfArms.value!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().padding(50.dp),
                                contentScale = ContentScale.Fit,
                                alpha = 1f
                            )
                        } else {
                            Loader()
                            Text(
                                text = "Loading image...",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getData(country: CountryInfoView): List<Pair<String,String>> {
    val propertiesArray = country.javaClass.kotlin.declaredMemberProperties
        .filter { property ->
            val clazz = property.returnType.classifier
            val propName = property.name
            clazz != Boolean::class && !propName.contains("flag") && !propName.contains("coat")
        }
        .sortedWith(
            compareBy {
                getPropertyPosition(it.name)
            }
        )
        .map { property ->
            Pair(
                getBeautifiedPropertyName(property.name),
                getPropertyValueAsString(property.name, country).capitalize()
            )
        }
        .toMutableList()
    if(country.unMember) {
        propertiesArray.add(
            Pair(
                "member",
                "UNESCO"
            )
        )
    }
    return propertiesArray
}

