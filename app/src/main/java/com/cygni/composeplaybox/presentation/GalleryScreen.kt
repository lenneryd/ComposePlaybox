package com.cygni.composeplaybox.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.cygni.composeplaybox.R
import com.cygni.composeplaybox.data.models.PhotoModel
import com.cygni.composeplaybox.presentation.colors.DarkColors
import com.cygni.composeplaybox.presentation.colors.LightColors
import com.cygni.composeplaybox.presentation.viewmodel.GalleryScreenState
import com.cygni.composeplaybox.presentation.viewmodel.GalleryViewModel

@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val uiState by viewModel.uiState.collectAsState(GalleryScreenState(emptyList()))
    GalleryScreenComposable(uiState = uiState)
}

@Composable
fun GalleryScreenComposable(uiState: GalleryScreenState) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {

        var anyImageLoaded by remember { mutableStateOf(false) }

        Crossfade(targetState = anyImageLoaded) { isAnyImageLoaded ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = uiState.photos) { photo ->
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        // Create refs for direct children
                        val (image, infobox, icon) = createRefs()

                        var isShowingInfo by remember { mutableStateOf(false) }
                        val painter = rememberImagePainter(photo.url,
                            builder = {
                                size(OriginalSize)
                                photo.placeholderRes?.let {
                                    placeholder(it)
                                }
                                crossfade(true)
                            })

                        if (painter.state is ImagePainter.State.Success) {
                            anyImageLoaded = true
                        }

                        Log.d("GalleryScreen", "Loading image: ${photo.url}")
                        Image(
                            painter = painter,
                            contentDescription = photo.title,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 200.dp)
                                .constrainAs(image) {
                                    height = Dimension.wrapContent
                                    width = Dimension.fillToConstraints
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                        )

                        Crossfade(
                            targetState = isShowingInfo,
                            modifier = Modifier
                                .constrainAs(infobox) {
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                    start.linkTo(image.start)
                                    end.linkTo(image.end)
                                    top.linkTo(image.top)
                                    bottom.linkTo(image.bottom)
                                }
                        ) { isShowing ->
                            if (isShowing) {
                                OverlayComposable(photo) {
                                    isShowingInfo = isShowingInfo.not()
                                }
                            }
                        }

                        Crossfade(
                            targetState = isShowingInfo,
                            modifier = Modifier
                                .constrainAs(icon) {
                                    end.linkTo(image.end)
                                    bottom.linkTo(image.bottom)
                                })
                        { isShowing ->
                            Icon(
                                if (isShowing) Icons.Filled.Info else Icons.Outlined.Info,
                                contentDescription = "Info Icon",
                                tint = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(bottom = 8.dp, end = 8.dp)
                                    .clickable { isShowingInfo = isShowingInfo.not() }
                            )
                        }
                    }
                }
            }

            if (!isAnyImageLoaded) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun OverlayComposable(photo: PhotoModel, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background.copy(alpha = 0.5f)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = photo.title,
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Flicker ID: ${photo.id}",
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )

            val context = LocalContext.current
            Text(
                text = photo.url,
                color = MaterialTheme.colors.primaryVariant,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    context.apply {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(photo.url)
                            )
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun GalleryPreview() {
    GalleryScreenComposable(
        uiState = galleryPreviewState()
    )
}

fun galleryPreviewState(): GalleryScreenState = GalleryScreenState(
    listOf(
        PhotoModel(
            id = "1",
            title = "Moon",
            url = "https://live.staticflickr.com/65535/51709199634_124a8612b0_z.jpg",
            placeholderRes = R.drawable.moon_placeholder
        ),
        PhotoModel(
            id = "2",
            title = "Saturnus",
            url = "https://live.staticflickr.com/65535/51699978771_da7d40866f_z.jpg",
            placeholderRes = R.drawable.saturnus_placeholder
        )
    )
)

