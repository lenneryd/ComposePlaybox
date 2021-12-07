package com.cygni.composeplaybox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cygni.composeplaybox.domain.usecase.ClockUseCase
import com.cygni.composeplaybox.presentation.viewmodel.ClockViewModel.Companion.FORMATTERS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val useCase: ClockUseCase
) : ViewModel() {

    val uiState: Flow<ClockScreenState> = useCase.getTime().map { Date(it) }.map { date ->
        ClockScreenState(date)
    }

    companion object {
        val FORMATTERS = Formatters()
    }
}

fun Date.mapTimeState(): ClockTimeState =
    ClockTimeState(
        weekday = FORMATTERS.weekday.format(this),
        dayMonth = FORMATTERS.dayMonth.format(this),
        hour = FORMATTERS.hour.format(this),
        minute = FORMATTERS.minute.format(this),
        second = FORMATTERS.second.format(this),
        millisecond = FORMATTERS.millisecond.format(this),
        numbers = this.toLocal().let {
            ClockTime(
                hour = it.hour,
                minute = it.minute,
                second = it.second,
                millisecond = it.get(ChronoField.MILLI_OF_SECOND)
            )
        }
    )

fun Date.mapStopWatchState(fromDate: Date?): ClockTime =
    fromDate?.let {
        val from = it.toLocal()
        val now = this.toLocal()
        val duration = Duration.between(from, now)
        return ClockTime(
            started = fromDate,
            hour = (duration.toHours() % 24).toInt(),
            minute = (duration.toMinutes() % 60).toInt(),
            second = (duration.seconds % 60).toInt(),
            millisecond = (duration.nano / 1000000)
        )
    } ?: ClockTime(null, 0, 0, 0, 0)

private fun Date.toLocal(): LocalDateTime =
    Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDateTime()

data class Formatters(
    val weekday: SimpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault()),
    val dayMonth: SimpleDateFormat = SimpleDateFormat("d MMM", Locale.getDefault()),
    val hour: SimpleDateFormat = SimpleDateFormat("HH", Locale.getDefault()),
    val minute: SimpleDateFormat = SimpleDateFormat("mm", Locale.getDefault()),
    val second: SimpleDateFormat = SimpleDateFormat("ss", Locale.getDefault()),
    val millisecond: SimpleDateFormat = SimpleDateFormat("SSS", Locale.getDefault())
)

data class ClockScreenState(
    val date: Date
)

data class ClockTimeState(
    val weekday: String = "",
    val dayMonth: String = "",
    val hour: String = "",
    val minute: String = "",
    val second: String = "",
    val millisecond: String = "",
    val numbers: ClockTime = ClockTime()
)

data class ClockTime(
    val started: Date? = null,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val millisecond: Int = 0,
)