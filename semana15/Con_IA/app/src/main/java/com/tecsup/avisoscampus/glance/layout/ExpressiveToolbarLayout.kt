package com.tecsup.avisoscampus.glance.layout

import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.unit.ColorProvider
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolBarLayoutDimens.minCenterButtonTapTarget
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolBarLayoutDimens.minCornerButtonTapTarget
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolBarLayoutDimens.scaledButtonBackground
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolBarLayoutDimens.scaledIconSize

/**
 * Canonical Layout de barra de herramientas expresiva con forma de galleta de 4 lados
 * que presenta las 5 acciones más frecuentes de AvisosCampus.
 */
@Composable
fun ExpressiveToolbarLayout(
    centerButton: ExpressiveToolBarButton,
    cornerButtons: List<ExpressiveToolBarButton>,
) {
    checkCornerButtonsListSize(cornerButtons)

    val widgetSize = LocalSize.current
    val cookieBackgroundSize = widgetSize.height.coerceAtMost(widgetSize.width)

    val backgroundModifier = GlanceModifier
        .size(cookieBackgroundSize)
        .appWidgetBackground()
        .fourSidedCookieBackground()

    when (ExpressiveToolbarLayoutSize.fromLocalSize()) {
        ExpressiveToolbarLayoutSize.SMALL -> CenterButtonOnlyLayout(
            centerButton = centerButton,
            modifier = backgroundModifier
        )

        ExpressiveToolbarLayoutSize.MEDIUM -> AllButtonsScaledLayout(
            centerButton = centerButton,
            cornerButtons = cornerButtons,
            cookieBackgroundSize = cookieBackgroundSize,
            modifier = backgroundModifier
        )
    }
}

@Composable
private fun GlanceModifier.fourSidedCookieBackground(): GlanceModifier {
    return this.background(
        imageProvider = ImageProvider(R.drawable.four_side_cookie_background),
        colorFilter = ColorFilter.tint(GlanceTheme.colors.widgetBackground)
    )
}

@Composable
private fun CenterButtonOnlyLayout(
    centerButton: ExpressiveToolBarButton,
    modifier: GlanceModifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CenterButton(
            button = centerButton,
            buttonBackgroundSize = 48.dp,
            clickableSize = 48.dp,
            iconSize = 24.dp,
            filled = false,
            shape = RoundedCornerShape.MEDIUM
        )
    }
}

@Composable
private fun AllButtonsScaledLayout(
    centerButton: ExpressiveToolBarButton,
    cornerButtons: List<ExpressiveToolBarButton>,
    cookieBackgroundSize: Dp,
    modifier: GlanceModifier,
) {
    val buttonBackgroundSize = scaledButtonBackground(cookieBackgroundSize)
    val iconSize = scaledIconSize(cookieBackgroundSize)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CenterButton(
            button = centerButton,
            buttonBackgroundSize = buttonBackgroundSize,
            clickableSize = minCenterButtonTapTarget,
            iconSize = iconSize,
            filled = Build.VERSION.SDK_INT > Build.VERSION_CODES.S,
            shape = RoundedCornerShape.MEDIUM
        )
        CornerButtonsGrid(
            cornerButtons = cornerButtons,
            buttonBackgroundSize = buttonBackgroundSize,
            iconSize = iconSize,
            clickableSize = buttonBackgroundSize.coerceAtLeast(minCornerButtonTapTarget),
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}

data class ExpressiveToolBarButton(
    @DrawableRes val iconRes: Int,
    val contentDescription: String,
    val onClick: Action,
    val text: String? = null,
)

@Composable
private fun CornerButtonsGrid(
    cornerButtons: List<ExpressiveToolBarButton>,
    buttonBackgroundSize: Dp,
    clickableSize: Dp,
    iconSize: Dp,
    modifier: GlanceModifier,
) {
    checkCornerButtonsListSize(cornerButtons)

    TwoRowGrid(
        spacing = 0.dp,
        modifier = modifier,
        items = cornerButtons.map {
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    CornerButton(
                        toolBarButton = it,
                        buttonBackgroundSize = buttonBackgroundSize,
                        iconSize = iconSize,
                        clickableSize = clickableSize
                    )
                }
            }
        },
    )
}

@Composable
private fun CornerButton(
    toolBarButton: ExpressiveToolBarButton,
    buttonBackgroundSize: Dp,
    clickableSize: Dp,
    iconSize: Dp,
) {
    IconButton(
        imageProvider = ImageProvider(toolBarButton.iconRes),
        contentDescription = toolBarButton.contentDescription,
        iconSize = iconSize,
        backgroundSize = buttonBackgroundSize,
        backgroundColor = androidx.glance.color.ColorProvider(Color.Transparent, Color.Transparent),
        contentColor = GlanceTheme.colors.primary,
        roundedCornerShape = RoundedCornerShape.FULL,
        onClick = toolBarButton.onClick,
        modifier = GlanceModifier.size(clickableSize)
    )
}

@Composable
private fun CenterButton(
    button: ExpressiveToolBarButton,
    clickableSize: Dp,
    buttonBackgroundSize: Dp,
    iconSize: Dp,
    shape: RoundedCornerShape,
    filled: Boolean,
) {
    val backgroundColor = if (filled) {
        GlanceTheme.colors.tertiary
    } else {
        androidx.glance.color.ColorProvider(Color.Transparent, Color.Transparent)
    }

    val contentColor = if (filled) {
        GlanceTheme.colors.onTertiary
    } else {
        GlanceTheme.colors.primary
    }

    IconButton(
        imageProvider = ImageProvider(button.iconRes),
        contentDescription = button.contentDescription,
        iconSize = iconSize,
        backgroundSize = buttonBackgroundSize,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        roundedCornerShape = shape,
        onClick = button.onClick,
        modifier = GlanceModifier.size(clickableSize)
    )
}

@Composable
private fun IconButton(
    imageProvider: ImageProvider,
    contentDescription: String,
    backgroundSize: Dp,
    iconSize: Dp,
    roundedCornerShape: RoundedCornerShape,
    onClick: Action,
    modifier: GlanceModifier,
    backgroundColor: ColorProvider,
    contentColor: ColorProvider,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .cornerRadius(roundedCornerShape.cornerRadius)
            .semantics { this.contentDescription = contentDescription }
            .clickable(onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = GlanceModifier
                .size(backgroundSize)
                .background(backgroundColor)
                .cornerRadius(roundedCornerShape.cornerRadius)
        ) {
            Image(
                provider = imageProvider,
                contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = GlanceModifier.size(iconSize)
            )
        }
    }
}

private enum class ExpressiveToolbarLayoutSize {
    SMALL,
    MEDIUM;

    companion object {
        @Composable
        fun fromLocalSize(): ExpressiveToolbarLayoutSize {
            val size = LocalSize.current
            val boxSize = size.width.coerceAtMost(size.height)
            return when {
                boxSize < 140.dp -> SMALL
                else -> MEDIUM
            }
        }
    }
}

private object ExpressiveToolBarLayoutDimens {
    val minCornerButtonTapTarget = 48.dp
    val minCenterButtonTapTarget = 60.dp

    fun scaledIconSize(backgroundSize: Dp) =
        Dp((13 * backgroundSize.value) / 100)

    fun scaledButtonBackground(backgroundSize: Dp) =
        Dp((26 * backgroundSize.value) / 100)
}

private fun checkCornerButtonsListSize(cornerButtons: List<ExpressiveToolBarButton>) {
    if (cornerButtons.size != 4) {
        Log.w("ExpressiveToolbarLayout", "Expected 4 corner buttons, but passed ${cornerButtons.size}")
    }
}
