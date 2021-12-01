package com.cygni.composeplaybox.domain.usecase

import com.cygni.composeplaybox.domain.repository.ClockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClockUseCase @Inject constructor(
    private val clockRepository: ClockRepository
) {

    fun getTime(): Flow<Long> = clockRepository.getTime()
}