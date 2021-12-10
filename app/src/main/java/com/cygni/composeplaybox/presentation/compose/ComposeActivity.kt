package com.cygni.composeplaybox.presentation.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cygni.composeplaybox.R
import com.cygni.composeplaybox.presentation.colors.AppTheme
import com.cygni.composeplaybox.presentation.colors.AppYuTheme
import com.cygni.composeplaybox.presentation.colors.DarkColors
import com.cygni.composeplaybox.presentation.colors.LightColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppYuTheme {
                MainScreenComposable(
                    clockContent = { ClockScreen(viewModel = viewModel()) },
                    galleryContent = { GalleryScreen(viewModel = viewModel()) }
                )
            }
        }
    }
}

@Preview
@Composable
fun ComposeActivityPreview() {
    AppYuTheme {
        MainScreenComposable(
            clockContent = { ClockScreenComposable(state = clockPreviewState()) },
            galleryContent = { GalleryScreenComposable(uiState = galleryPreviewState()) }
        )
    }
}