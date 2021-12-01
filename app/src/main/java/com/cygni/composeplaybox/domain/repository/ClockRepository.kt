package com.cygni.composeplaybox.domain.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.sample
import javax.inject.Inject

class ClockRepository @Inject constructor(
    @TimeProvider
    val timestampProvider: () -> Long
) {

    fun getTime(): Flow<Long> = flow {
        while (true) {
            emit(timestampProvider())
            delay(100)
        }
    }.sample(200)
}