package com.cygni.composeplaybox.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cygni.composeplaybox.domain.repository.ClockRepository
import com.cygni.composeplaybox.domain.usecase.ClockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    useCase: ClockUseCase
) : ViewModel() {
    private val formatters = Formatters()

    val uiState: Flow<ClockScreenState> = useCase.getTime().map { Date(it) }.map { date ->
        ClockScreenState(
            weekday = formatters.weekday.format(date),
            dayMonth = formatters.dayMonth.format(date),
            hour = formatters.hour.format(date),
            minute = formatters.minute.format(date),
            second = formatters.second.format(date),
            numbers = Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalTime()
                .let {
                    ClockTime(
                        minute = it.minute,
                        second = it.second
                    )
                }
        )
    }
}

data class Formatters(
    val weekday: SimpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault()),
    val dayMonth: SimpleDateFormat = SimpleDateFormat("d MMM", Locale.getDefault()),
    val hour: SimpleDateFormat = SimpleDateFormat("HH", Locale.getDefault()),
    val minute: SimpleDateFormat = SimpleDateFormat("mm", Locale.getDefault()),
    val second: SimpleDateFormat = SimpleDateFormat("ss", Locale.getDefault())

)

data class ClockScreenState(
    val weekday: String = "",
    val dayMonth: String = "",
    val hour: String = "",
    val minute: String = "",
    val second: String = "",
    val numbers: ClockTime = ClockTime()
)

data class ClockTime(
    val minute: Int = 0,
    val second: Int = 0
)