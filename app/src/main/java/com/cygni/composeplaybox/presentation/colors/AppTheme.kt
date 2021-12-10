package com.cygni.composeplaybox.presentation.colors

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cygni.composeplaybox.presentation.colors.builder.AppTypography
import com.cygni.composeplaybox.presentation.colors.builder.DarkThemeColors
import com.cygni.composeplaybox.presentation.colors.builder.LightThemeColors

@Composable
fun AppTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    androidx.compose.material.MaterialTheme(
        colors = if (dark) DarkColors else LightColors,
        content = content
    )
}

@Composable
fun AppYuTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (dark) dynamicLightColorScheme(context) else dynamicDarkColorScheme(context)
    } else {
        if (dark) DarkThemeColors else LightThemeColors
    }

    MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
}