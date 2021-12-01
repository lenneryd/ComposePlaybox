package com.cygni.composeplaybox.data.api

import com.cygni.composeplaybox.data.api.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/")
    suspend fun getSearchByTag(
        @Query("api_key") apiKey: String,
        @Query("method") method: String = "flickr.photos.search",
        @Query("format") format: String = FORMAT_JSON,
        @Query("nojsoncallback") noJsonCallback: Int = JSON_CALLBACK_NO,
        @Query("privacy_filter") privacy: Int = PRIVACY_PUBLIC,
        @Query("safe_search") safeSearch: Int = SAFE_SEARCH_SAFE,
        @Query("content_type") contentType: Int = CONTENT_TYPE_PHOTOS,
        @Query("sort") sortType: String = SORT_RELEVANCE,
        @Query("per_page") resultsPerPage: Int = 30,
        @Query("page") page: Int = 1,
        @Query("tags") tags: String
    ): Response<SearchResponse>

    companion object {
        private const val PRIVACY_PUBLIC = 1
        private const val SAFE_SEARCH_SAFE = 1
        private const val CONTENT_TYPE_PHOTOS = 1
        private const val SORT_RELEVANCE = "relevance"
        private const val FORMAT_JSON = "json"
        private const val JSON_CALLBACK_NO = 1
    }
}