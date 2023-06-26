package com.example.myapplication.ui.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToggleButtonRow(leftButtonText: String, rightButtonText: String, selectedButton: MutableState<Int>) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleButton(
            modifier = Modifier
                .weight(1f),
            text = leftButtonText,
            isSelected = selectedButton.value == 0,
            onClick = { selectedButton.value = 0 }
        )
        ToggleButton(
            modifier = Modifier
                .weight(1f),
            text = rightButtonText,
            isSelected = selectedButton.value == 1,
            onClick = { selectedButton.value = 1 }
        )
    }
}

@Composable
fun ToggleButton(
    modifier: Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .height(50.dp)
            .drawWithContent {
                drawContent()
                if (isSelected) {
                    val lineWidth = 5f
                    val offsetY = size.height - lineWidth

                    drawLine(
                        color = selectedColor,
                        start = Offset(0f, offsetY),
                        end = Offset(size.width, offsetY),
                        strokeWidth = lineWidth
                    )
                }
            }
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            onClick = onClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = text,
                fontSize = 15.sp
            )
        }
    }
}