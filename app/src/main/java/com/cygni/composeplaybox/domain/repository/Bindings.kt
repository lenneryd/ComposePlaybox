package com.cygni.composeplaybox.domain.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeProvider

@Module
@InstallIn(ViewModelComponent::class)
object TimeProviderModule {

    @TimeProvider
    @Provides
    fun timeProvider(): () -> Long = { System.currentTimeMillis() }
}

@Module
@InstallIn(ViewModelComponent::class)
object ClockRepositoryModule {

    @Provides
    fun clockRepository(@TimeProvider timeProvider: () -> Long): ClockRepository = ClockRepository(
        timeProvider
    )
}