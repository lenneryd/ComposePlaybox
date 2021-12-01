package com.cygni.composeplaybox.domain.usecase

import com.cygni.composeplaybox.data.Outcome
import com.cygni.composeplaybox.data.models.SearchResultModel
import com.cygni.composeplaybox.data.models.toModel
import com.cygni.composeplaybox.domain.repository.SearchByTagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GalleryUseCase @Inject constructor(
    private val repo: SearchByTagRepository
) {
    fun get(params: RequestParams): Flow<Outcome<SearchResultModel>> = repo.get(
        SearchByTagRepository.RequestParams(params.tag, params.page, 50)
    ).map { result ->
        when (result) {
            is Outcome.Success -> {
                // We got data, we'll map it from an Api model to a data model here.
                Outcome.Success(result.result.photos.toModel())
            }

            is Outcome.Error -> result
        }
        // We want our repository and Api requests to occur on the IO dispatcher.
    }.flowOn(Dispatchers.IO)

    // No real need for multiple request param classes for First and Page, but if one moves the
    // paging logic into the UseCase, then it becomes more of an interesting discussion.
    // Since then the UseCase would need to juggle existing data and new data, and combine it to something the
    // ViewModel can easily use.
    sealed class RequestParams(open val tag: String, open val page: Int) {
        data class First(override val tag: String) : RequestParams(tag, 1)
        data class Page(override val tag: String, override val page: Int) : RequestParams(tag, page)
    }
}