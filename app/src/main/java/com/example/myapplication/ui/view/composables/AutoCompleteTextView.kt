package com.example.myapplication.ui.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    isSuggestionMenuOpen: Boolean,
    itemContent: @Composable (T) -> Unit = {},
) {

    val view = LocalView.current
    val lazyListState = rememberLazyListState()

    QuerySearch(
        query = query,
        label = queryLabel,
        onQueryChanged = onQueryChanged,
        onDoneActionClick = {
            view.clearFocus()
            onDoneActionClick()
        },
        onClearClick = {
            onClearClick()
        },
        modifier = modifier
    )

    if(isSuggestionMenuOpen) {
        LazyColumn(
            state = lazyListState,
            modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
        ) {
            if (predictions.isNotEmpty()) {
                itemsIndexed(predictions) { _, prediction ->
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                view.clearFocus()
                                onItemClick(prediction)
                            }
                    ) {
                        itemContent(prediction)
                    }
                }
            }
        }
    }
}