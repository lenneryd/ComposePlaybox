package com.cygni.composeplaybox.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.Crossfade
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
import com.cygni.composeplaybox.presentation.viewmodel.mapStopWatchState
import com.cygni.composeplaybox.presentation.viewmodel.mapTimeState
import java.time.Instant
import java.util.*

@Composable
fun ClockScreen(
    viewModel: ClockViewModel
) {
    val uiState by viewModel.uiState.collectAsState(ClockScreenState(Date()))
    ClockScreenComposable(state = uiState)
}

@Composable
fun ClockScreenComposable(
    state: ClockScreenState,
    preStartedStopwatch: Date? = null
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // Create refs for direct children
            val (background, clockBox, stopwatch, stopwatchBackground) = createRefs()
            var useStroke by remember { mutableStateOf(false) }
            var stopWatchRunningState by remember {
                mutableStateOf(
                    preStartedStopwatch?.let { StopWatchRunningState.Started(it) }
                        ?: StopWatchRunningState.Empty
                )
            }

            val clockState = state.date.mapTimeState()
            ClockGraphicsComposable(
                state = ClockGraphicsState(
                    second = clockState.numbers.second,
                    color = MaterialTheme.colors.surface,
                    fillCircle = clockState.numbers.minute % 2 == 0,
                    useStroke = useStroke
                ),
                modifier = Modifier.constrainAs(background) {
                    top.linkTo(clockBox.top)
                    bottom.linkTo(clockBox.bottom)
                    start.linkTo(clockBox.start)
                    end.linkTo(clockBox.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
            ) {
                useStroke = useStroke.not()
                stopWatchRunningState = stopWatchRunningState.toggle(Date())
            }

            ClockTextComposable(
                state = ClockTextState(
                    clockState.second,
                    clockState.minute,
                    clockState.hour,
                    clockState.weekday,
                    clockState.dayMonth
                ),
                modifier = Modifier
                    .constrainAs(clockBox) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            stopWatchRunningState.let { runningState ->

                val timeToShow = when (runningState) {
                    is StopWatchRunningState.Started -> {
                        state.date.mapStopWatchState(fromDate = runningState.startedAt)
                    }
                    is StopWatchRunningState.Stopped -> {
                        runningState.stoppedAt.mapStopWatchState(fromDate = runningState.startedAt)
                    }
                    else -> null
                }

                if (timeToShow != null) {
                    StopWatchComposable(
                        state = timeToShow.toStopWatchState(),
                        modifier = Modifier
                            .constrainAs(stopwatch) {
                                top.linkTo(clockBox.bottom, margin = 16.dp)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    ClockGraphicsComposable(
                        state = ClockGraphicsState(
                            second = timeToShow.second,
                            color = MaterialTheme.colors.surface,
                            fillCircle = timeToShow.minute % 2 == 0,
                            useStroke = true
                        ),
                        modifier = Modifier.constrainAs(stopwatchBackground) {
                            top.linkTo(stopwatch.top)
                            bottom.linkTo(stopwatch.bottom)
                            start.linkTo(stopwatch.start)
                            end.linkTo(stopwatch.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        },
                        onClick = {
                            stopWatchRunningState = stopWatchRunningState.toggle(Date())
                        }
                    )
                }
            }
        }
    }
}

fun StopWatchRunningState.toggle(now: Date): StopWatchRunningState =
    when (this) {
        is StopWatchRunningState.Empty -> StopWatchRunningState.Started(
            startedAt = now
        )
        is StopWatchRunningState.Stopped -> StopWatchRunningState.Started(
            startedAt = now,
        )
        is StopWatchRunningState.Started -> StopWatchRunningState.Stopped(
            startedAt = startedAt,
            stoppedAt = now
        )
    }

sealed class StopWatchRunningState {
    object Empty : StopWatchRunningState()
    data class Started(val startedAt: Date, val offset: Long = 0) : StopWatchRunningState()
    data class Stopped(val startedAt: Date, val stoppedAt: Date, val offset: Long = 0) :
        StopWatchRunningState()
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

data class ClockGraphicsState(
    val second: Int,
    val color: Color,
    val fillCircle: Boolean = true,
    val useStroke: Boolean = true
)

@Composable
fun ClockGraphicsComposable(
    state: ClockGraphicsState,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .aspectRatio(1.0f, matchHeightConstraintsFirst = true)
            .clickable { onClick() }
    ) {
        val color: Color = MaterialTheme.colors.surface
        val sweep = sweepAngle(second = state.second).value * 360f
        val sweepAngle = if (state.fillCircle) sweep else 360f - sweep
        val startAngle = if (state.fillCircle) -90f else -90f + sweep
        val drawStyle = if (!state.useStroke) Fill else Stroke(36.0f)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable { onClick() },
            onDraw = {
                if (state.second == 0) {
                    // In this special case we've just changed to the next minute on second 0.
                    if (!state.fillCircle && !state.useStroke) {
                        drawCircle(color = color)
                    }
                } else {
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = !state.useStroke,
                        style = drawStyle
                    )
                }
            }
        )
    }
}

data class ClockTextState(
    val second: String,
    val minute: String,
    val hour: String,
    val weekday: String,
    val dayMonth: String
)

@Composable
fun ClockTextComposable(state: ClockTextState, modifier: Modifier) {
    Box(
        modifier = modifier.padding(32.dp)
    ) {
        Column {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = "${state.hour}:${state.minute}:",
                    fontSize = 22.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    text = state.second,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.alignByBaseline()
                )
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = state.weekday,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = state.dayMonth,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}

fun ClockTime.toStopWatchState() = StopWatchState(
    millisecond = millisecond.takeUnless { it < 0 }?.let { "$millisecond" }.orEmpty()
        .padStart(3, '0'),
    second = second.takeUnless { it < 0 }?.let { "$second" }.orEmpty().padStart(2, '0'),
    minute = "$minute".padStart(2, '0'),
    hour = "$hour".padStart(2, '0'),
)

data class StopWatchState(
    val millisecond: String,
    val second: String,
    val minute: String,
    val hour: String,
)

@Composable
fun StopWatchComposable(state: StopWatchState, modifier: Modifier) {
    Box(
        modifier = modifier.padding(32.dp)
    ) {
        Column {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = "${state.hour}:${state.minute}:${state.second}",
                    fontSize = 22.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.alignByBaseline(),
                )
            }
            Text(
                text = state.millisecond,
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StopWatchPreview() {
    ClockScreenComposable(
        state = stopwatchPreviewState(), preStartedStopwatch =
        Date.from(
            Instant.ofEpochMilli(Date().time)
                .minusSeconds(70)
                .minusMillis(12)
        )
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ClockScreenPreview() {
    ClockScreenComposable(
        state = clockPreviewState()
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ClockScreenNightPreview() {
    ClockScreenComposable(
        state = clockPreviewState()
    )
}

fun stopwatchPreviewState(): ClockScreenState = ClockScreenState(Date())
fun clockPreviewState(): ClockScreenState = ClockScreenState(Date())