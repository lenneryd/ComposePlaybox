package com.cygni.composeplaybox.presentation

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
import androidx.compose.material.Colors
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
import com.cygni.composeplaybox.presentation.colors.DarkColors
import com.cygni.composeplaybox.presentation.colors.LightColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreenComposable(
                clockContent = { ClockScreen(viewModel = viewModel()) },
                galleryContent = { GalleryScreen(viewModel = viewModel()) }
            )
        }
    }
}

enum class Views {
    Clock, Gallery, None
}

@Composable
fun MainScreenComposable(
    clockContent: @Composable () -> Unit,
    galleryContent: @Composable () -> Unit
) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColors else LightColors) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        var showView by remember { mutableStateOf(Views.Clock) }
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                // Topbar
                // background = Uses Primary for light, and surface for dark by default.
                // content = Uses onPrimary for light, and onSurface for dark by default.
                TopAppBar(
                    title = { Text("Compose Playbox") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.apply { if (isClosed) open() else close() }
                            }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu Icon",
                            )
                        }
                    })
            },
            // Drawer
            // background = Uses surface color by default on both light and dark.
            // scrim = Uses onSurface with alpha.
            drawerContent = {
                Text(
                    text = "Playground Items", fontSize = 24.sp, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)

                )
                DrawerItem(iconResource = R.drawable.image, title = "Flickr Gallery") {
                    if (showView != Views.Gallery) {
                        showView = Views.Gallery
                    }
                    scope.launch { scaffoldState.drawerState.apply { close() } }
                }
                Divider(startIndent = 16.dp, modifier = Modifier.padding(end = 16.dp))
                DrawerItem(iconResource = R.drawable.clock_outline, title = "Clock Example") {
                    if (showView != Views.Clock) {
                        showView = Views.Clock
                    }
                    scope.launch { scaffoldState.drawerState.apply { close() } }
                }
                Divider(startIndent = 16.dp, modifier = Modifier.padding(end = 16.dp))
            }
        ) {
            Crossfade(targetState = showView) { showView ->
                when (showView) {
                    Views.Gallery -> galleryContent()
                    Views.Clock -> clockContent()
                    Views.None -> {}
                }
            }
        }
    }
}

@Composable
fun Color.orIfDarkTheme(color: Color) = if (isSystemInDarkTheme()) this else color

@Composable
fun DrawerItem(iconResource: Int, title: String, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp)
        .clickable { onClick() }) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = title,
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreenComposable(
        clockContent = { ClockScreenComposable(state = clockPreviewState()) },
        galleryContent = { GalleryScreenComposable(uiState = galleryPreviewState()) }
    )
}