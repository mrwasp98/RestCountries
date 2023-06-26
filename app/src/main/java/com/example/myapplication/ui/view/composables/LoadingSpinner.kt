package com.example.myapplication.ui.view.composables

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.myapplication.R

@Composable
fun LoadingSpinner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun Loader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf20_vamhepnn))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 100,
        reverseOnRepeat = true
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        alignment = Alignment.TopCenter,
        modifier = modifier
    )
}





