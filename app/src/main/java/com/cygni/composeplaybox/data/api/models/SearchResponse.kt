package com.cygni.composeplaybox.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val photos: SearchPhotosEntity
)

@Serializable
data class SearchPhotosEntity(
    val page: Int,
    val pages: String,
    val perpage: Int,
    val total: String,
    val photo: List<PhotoEntity>
)

@Serializable
data class PhotoEntity(
    val id: String,
    val server: String,
    val title: String,
    val secret: String
)