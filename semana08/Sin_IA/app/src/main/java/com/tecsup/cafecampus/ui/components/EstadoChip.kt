package com.tecsup.cafecampus.ui.components

import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EstadoChip(estado: String) {
    SuggestionChip(
        onClick = { },
        label = { Text(estado) }
    )
}
