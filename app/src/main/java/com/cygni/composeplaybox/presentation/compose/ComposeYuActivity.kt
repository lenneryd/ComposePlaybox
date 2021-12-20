package com.cygni.composeplaybox.presentation.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cygni.composeplaybox.presentation.colors.AppYuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeYuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppYuTheme {
                MainScreenComposable(
                    clockContent = { ClockScreen(viewModel = viewModel()) },
                    galleryContent = { GalleryScreen(viewModel = viewModel()) },
                    colorsContent = { ColorsScreenComposable() }
                )
            }
        }
    }
}

@Preview
@Composable
fun ComposeYuActivityPreview() {
    AppYuTheme {
        MainScreenComposable(
            clockContent = { ClockScreenComposable(state = clockPreviewState()) },
            galleryContent = { GalleryScreenComposable(uiState = galleryPreviewState()) },
            colorsContent = { ColorsScreenComposable() }
        )
    }
}