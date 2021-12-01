package com.cygni.composeplaybox.domain.usecase

import com.cygni.composeplaybox.domain.repository.ClockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ClockUseCaseModule {
    @Provides
    fun clockUseCase(repository: ClockRepository): ClockUseCase = ClockUseCase(repository)
}

