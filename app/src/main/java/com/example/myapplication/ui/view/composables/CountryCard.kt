package com.example.myapplication.ui.view.composables

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.CountryInfo
import com.example.myapplication.data.model.CountryInfoView

@ExperimentalMaterial3Api
@Composable
fun CountryCard(
    country: CountryInfoView,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(PaddingValues(4.dp)),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace with your flag image
            country.flag.let {
                Surface(
                    modifier = Modifier
                        .width(64.dp)
                        .height(48.dp)
                        .padding(end = 16.dp),
                    shape = CircleShape,
                    color = Color.Gray // Placeholder color
                ) {
                    Image(
                        bitmap = it!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            Column {
                Text(
                    text = country.commonName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    color = LocalContentColor.current
                )
                Text(
                    text = country.officialName, // Replace with your desired text
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = country.continent, // Replace with your desired text
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}