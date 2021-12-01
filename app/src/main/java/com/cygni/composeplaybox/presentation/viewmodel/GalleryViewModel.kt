package com.cygni.composeplaybox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cygni.composeplaybox.data.Outcome
import com.cygni.composeplaybox.data.models.PhotoModel
import com.cygni.composeplaybox.domain.usecase.GalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    useCase: GalleryUseCase
) : ViewModel() {

    val uiState: Flow<GalleryScreenState> =
        useCase.get(GalleryUseCase.RequestParams.First("NASA")).map {
            when (it) {
                is Outcome.Success -> {
                    GalleryScreenState(it.result.photo)
                }
                is Outcome.Error -> {
                    GalleryScreenState(emptyList())
                }
            }
        }
}

data class GalleryScreenState(
    val photos: List<PhotoModel>
)