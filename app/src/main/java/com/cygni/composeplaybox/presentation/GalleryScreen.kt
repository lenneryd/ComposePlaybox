package com.cygni.composeplaybox.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.cygni.composeplaybox.R
import com.cygni.composeplaybox.data.models.PhotoModel
import com.cygni.composeplaybox.presentation.viewmodel.GalleryScreenState
import com.cygni.composeplaybox.presentation.viewmodel.GalleryViewModel

private val White1000 = Color(0xFFFFFFFF)
private val Yellow200 = Color(0xffffeb46)
private val Yellow500 = Color(0xFFD37510)
private val Blue200 = Color(0xff91a4fc)
private val Blue500 = Color(0xFF0D258B)
private val Blue700 = Color(0xFF06103C)

private val DarkColors = darkColors(
    primary = Yellow200,
    primaryVariant = Yellow200,
    secondary = Yellow500,
    surface = Blue500,
    background = Blue700
    // ...
)
private val LightColors = lightColors(
    primary = Blue200,
    primaryVariant = Blue200,
    surface = Blue700,
    secondary = Blue700,
    background = Blue700
    // ...
)

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
                    Log.d("GalleryScreen", "Loading image: ${photo.url}")
                    Image(
                        painter = rememberImagePainter(photo.url, builder = {
                            size(OriginalSize)
                            photo.placeholderRes?.let {
                                placeholder(it)
                            }
                        }),
                        contentDescription = photo.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.constrainAs(image) {
                            height = Dimension.wrapContent
                            width = Dimension.fillToConstraints
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )

                    if (isShowingInfo) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background.copy(alpha = 0.5f)
                                )
                                .fillMaxSize()
                                .padding(24.dp)
                                .constrainAs(infobox) {
                                    height = Dimension.fillToConstraints
                                    width = Dimension.fillToConstraints
                                    start.linkTo(image.start)
                                    end.linkTo(image.end)
                                    top.linkTo(image.top)
                                    bottom.linkTo(image.bottom)
                                }
                                .clickable { isShowingInfo = isShowingInfo.not() }
                        ) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()) {
                                Column {
                                    Text(
                                        text = photo.title,
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier
                                        .fillMaxSize()

                                ) {

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
                    }

                    Icon(
                        if (isShowingInfo) Icons.Filled.Info else Icons.Outlined.Info,
                        contentDescription = "Info Icon",
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp, end = 8.dp)
                            .clickable { isShowingInfo = isShowingInfo.not() }
                            .constrainAs(icon) {
                                end.linkTo(image.end)
                                bottom.linkTo(image.bottom)
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GalleryPreview() {
    GalleryScreenComposable(
        uiState = GalleryScreenState(
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
    )
}

