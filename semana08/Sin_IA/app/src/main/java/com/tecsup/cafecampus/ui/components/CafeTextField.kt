package com.tecsup.cafecampus.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CafeTextField(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        modifier = modifier,
        visualTransformation = visualTransformation,
        singleLine = true
    )
}
