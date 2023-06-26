package com.example.myapplication.ui.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun CountryDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(16.dp)) {
        val icon = when(label){
            "Currencies" -> R.drawable.money
            "Timezones" -> R.drawable.clock
            "Car Side" -> R.drawable.car
            "Population" -> R.drawable.people
            "Languages" -> R.drawable.language
            "Area" -> R.drawable.area
            "Continent" -> R.drawable.continent
            "Latitude/Longitude" -> R.drawable.position
            "member" -> R.drawable.unesco_logo
            else -> null
        }
        if(icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Dollar Icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(32.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LocalContentColor.current
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}