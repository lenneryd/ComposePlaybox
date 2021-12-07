package com.cygni.composeplaybox.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cygni.composeplaybox.presentation.colors.DarkColors
import com.cygni.composeplaybox.presentation.colors.LightColors
import com.cygni.composeplaybox.presentation.viewmodel.ClockScreenState
import com.cygni.composeplaybox.presentation.viewmodel.ClockTime
import com.cygni.composeplaybox.presentation.viewmodel.ClockViewModel

@Composable
fun ClockScreen(
    viewModel: ClockViewModel
) {
    val uiState by viewModel.uiState.collectAsState(ClockScreenState())
    ClockScreenComposable(uiState = uiState)
}

@Composable
fun ClockScreenComposable(
    uiState: ClockScreenState
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // Create refs for direct children
            val (background, clockBox) = createRefs()
            var clicks by remember { mutableStateOf(0) }

            Box(
                modifier = Modifier
                    .constrainAs(background) {
                        top.linkTo(clockBox.top)
                        bottom.linkTo(clockBox.bottom)
                        start.linkTo(clockBox.start)
                        end.linkTo(clockBox.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .aspectRatio(1.0f, matchHeightConstraintsFirst = true)
                    .clickable { clicks++ }
            ) {
                val color: Color = MaterialTheme.colors.surface
                val isFillingCircle = uiState.numbers.minute % 2 == 0
                val isUsingStroke =
                    if (MaterialTheme.colors.isLight) clicks % 2 == 0 else clicks % 2 == 1

                val sweep = sweepAngle(second = uiState.numbers.second).value * 360f
                val sweepAngle = if (isFillingCircle) sweep else 360f - sweep
                val startAngle = if (isFillingCircle) -90f else -90f + sweep
                val drawStyle = if (!isUsingStroke) Fill else Stroke(24.0f)

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable { clicks++ },
                    onDraw = {
                        if (uiState.numbers.second == 0) {
                            // In this special case we've just changed to the next minute on second 0.
                            if (!isFillingCircle && !isUsingStroke) {
                                drawCircle(color = color)
                            }
                        } else {
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = !isUsingStroke,
                                style = drawStyle
                            )
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(clockBox) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(32.dp)
            ) {
                Column {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = "${uiState.hour}:${uiState.minute}:",
                            fontSize = 22.sp,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.alignByBaseline(),
                        )
                        Text(
                            text = uiState.second,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = uiState.weekday,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = uiState.dayMonth,
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun sweepAngle(second: Int): State<Float> {
    return animateFloatAsState(
        targetValue = (second.toFloat() + 0.5f) / 60f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
    )
}

@Preview
@Composable
fun ClockScreenPreview() {
    ClockScreenComposable(
        uiState = clockPreviewState()
    )
}

fun clockPreviewState(): ClockScreenState = ClockScreenState(
    weekday = "Monday",
    dayMonth = "25 NOV",
    hour = "16",
    minute = "15",
    second = "15",
    numbers = ClockTime(minute = 1, second = 15)
)