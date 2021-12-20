package com.cygni.composeplaybox.presentation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cygni.composeplaybox.presentation.viewmodel.asColorItems

@Composable
fun ColorsScreenComposable() {
    val colors = MaterialTheme.colorScheme
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        items(items = colors.asColorItems()) { colorItem ->
            ColorBox(
                textColor = colorItem.textColor,
                backgroundColor = colorItem.backgroundColor,
                title = colorItem.title
            )
        }
    }
}

@Composable
fun ColorBox(textColor: Color, backgroundColor: Color, title: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                border = BorderStroke(
                    3.dp,
                    MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
