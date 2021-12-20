package com.cygni.composeplaybox.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties

data class ColorItem(
    val colorName: String,
    val textColor: Color,
    val backgroundColor: Color,
    val title: String
)

fun ColorScheme.asColorItems(): List<ColorItem> {
    val allColors = ColorScheme::class.declaredMemberProperties
        .filter { it.returnType == Color::class.createType() }

    val (onColors, colors) = allColors.partition { it.name.startsWith("on") }
    val primary = colors.first { it.name.lowercase() == "primary" }

    val mapping = colors.associateWith { color ->
        (onColors.firstOrNull { onColor ->
            onColor.name.lowercase().contains(color.name.lowercase())
        })
    }

    return mapping.toList().map { (color, onColor) ->
        ColorItem(
            color.name,
            textColor = onColor?.get(this) as? Color ?: primary.get(this) as Color,
            backgroundColor = color.get(this) as Color,
            color.name.replaceFirstChar(Char::titlecase)
        )
    }.sortedBy { it.colorName.toColorOrder().order }
}

enum class ColorOrder(val identifier: String, val order: Int) {
    Primary("primary", 0),
    Secondary("secondary", 1),
    Tertiary("tertiary", 2),
    Error("error", 3),
    Background("background", 4),
    Surface("surface", 5),
    Outline("outline", 6),
    Unknown(identifier = "", 7)
}

fun String.toColorOrder(): ColorOrder {
    return ColorOrder.values().first { this.contains(it.identifier) }
}