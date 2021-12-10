package com.cygni.composeplaybox.presentation.colors

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Purple700 = Color(0xFF3700B3)
private val Purple200 = Color(0xFFBB86FC)
private val Purple300 = Color(0xFF9575CD)
private val Purple400 = Color(0xFFAB47BC)
private val Purple500 = Color(0xFF6200EE)
private val Purple600 = Color(0xFF8E24AA)
private val Purple900 = Color(0xFF4A148C)
private val Teal300 = Color(0xFF4DB6AC)
private val Teal400 = Color(0xFF26A69A)
private val White1000 = Color(0xFFFFFFFF)
private val Red500 = Color(0xFFF44336)


private val Black300 = Color(0xFFE0E0E0)
private val Black400 = Color(0xFFBDBDBD)
private val Black600 = Color(0xFF757575)
private val Black800 = Color(0xFF424242)
private val Black900 = Color(0xFF212121)
private val Black1000 = Color(0xFF000000)

val DarkColors = darkColors(
    primary = Purple300,
    primaryVariant = Purple400,
    secondary = Teal300,
    secondaryVariant = Black300,
    background = Black1000,
    surface = Black900,
    error = Red500,
    onPrimary = White1000,
    onSecondary = Black1000,
    onBackground = Black800,
    onSurface = White1000,
    onError = Red500
)

val LightColors = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal300,
    secondaryVariant = Teal400,
    background = White1000,
    surface = Purple200,
    error = Red500,
    onPrimary = White1000,
    onSecondary = Black1000,
    onBackground = Black1000,
    onSurface = Black1000,
    onError = White1000
)

val DarkYuColors = darkColorScheme()
val LightYuColors = lightColorScheme()
