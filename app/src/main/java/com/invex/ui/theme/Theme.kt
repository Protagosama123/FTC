package com.invex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val InvexColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = SoftWhite,
    background = MobileBackground,
    onBackground = IconColor,
    surface = HoverSelect,
    onSurface = IconColor,
    outline = OutlineShadow,
)

@Composable
fun InvexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = InvexColorScheme,
        typography = Typography,
        content = content,
    )
}
