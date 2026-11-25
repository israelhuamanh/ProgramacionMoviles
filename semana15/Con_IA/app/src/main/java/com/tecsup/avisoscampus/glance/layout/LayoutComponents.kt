package com.tecsup.avisoscampus.glance.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding

/**
 * Organiza los elementos horizontalmente con el espaciado indicado.
 */
@Composable
fun SpacedRow(
    items: List<@Composable () -> Unit>,
    spacing: Dp,
    modifier: GlanceModifier = GlanceModifier.fillMaxWidth(),
) {
    val padding = spacing / 2

    Column(modifier = modifier) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
        ) {
            items.forEachIndexed { index, item ->
                val paddingModifier = when (index) {
                    0 -> GlanceModifier.padding(end = padding)
                    items.lastIndex -> GlanceModifier.padding(start = padding)
                    else -> GlanceModifier.padding(start = padding, end = padding)
                }

                Box(
                    modifier = paddingModifier
                        .fillMaxHeight()
                        .defaultWeight()
                ) {
                    item()
                }
            }
        }
    }
}

/**
 * Organiza los elementos en una cuadrícula de hasta 2 filas en el espacio disponible.
 */
@Composable
fun TwoRowGrid(
    items: List<@Composable () -> Unit>,
    spacing: Dp,
    modifier: GlanceModifier = GlanceModifier.fillMaxSize(),
) {
    val middle = items.size / 2
    val rowOneItems = items.subList(0, middle)
    val rowTwoItems = items.subList(middle, items.size)

    Column(modifier = modifier) {
        if (rowOneItems.isNotEmpty()) {
            SpacedRow(
                items = rowOneItems,
                spacing = spacing,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .defaultWeight()
                    .padding(bottom = spacing / 2),
            )
        }
        if (rowTwoItems.isNotEmpty()) {
            SpacedRow(
                items = rowTwoItems,
                spacing = spacing,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(top = spacing / 2)
                    .defaultWeight()
            )
        }
    }
}
