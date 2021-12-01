package com.cygni.composeplaybox.domain.repository

import com.cygni.composeplaybox.data.ApiKey
import com.cygni.composeplaybox.data.Outcome
import com.cygni.composeplaybox.data.api.FlickrApi
import com.cygni.composeplaybox.data.api.models.SearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class SearchByTagRepository @Inject constructor(
    private val apiKey: ApiKey,
    private val api: FlickrApi
) {

    fun get(params: RequestParams): Flow<Outcome<SearchResponse>> = flow {
        val result: Outcome<SearchResponse> = apiCall {
            api.getSearchByTag(
                apiKey = apiKey.key,
                tags = params.tag,
                resultsPerPage = params.perPage,
                page = params.page
            )
        }
        emit(result)
    }

    data class RequestParams(val tag: String, val page: Int, val perPage: Int)
}

/**
 * Wraps a Retrofit Api call within a lambda expression, allowing the return value to be an [Outcome] of whatever
 * result is returned by the Api.
 * This abstracts the Retrofit specific code, such as Response, responseCode etc.
 * If more information is of interest for error display purposes, then the provided exception within Outcome.Error() can be customized
 * and checked by the view to show data.
 *
 * A mapper can also easily be added here, allowing the mapping between Api Entity objects to Domain Models to happen right here.
 * Then mapping errors would also be propagated by this method, for a more rigid Data -> Domain -> Presentation flow.
 */
suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Outcome<T> {
    return try {
        call.invoke().let { response ->
            if (response.isSuccessful) {
                response.body()?.let { entity ->
                    Outcome.Success(entity)
                } ?: Outcome.Error("Body cannot be null on a successful response", null)
            } else {
                Outcome.Error(
                    "Api error with errorCode: ${response.code()}",
                    Exception("Api error with errorCode: ${response.code()}")
                )
            }
        }
    } catch (e: Throwable) {
        Outcome.Error("Received Exception as part of api call", e)
    }
}