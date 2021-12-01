package com.cygni.composeplaybox.data.models

import com.cygni.composeplaybox.data.api.models.PhotoEntity
import com.cygni.composeplaybox.data.api.models.SearchPhotosEntity

data class SearchResultModel(
    val page: Int,
    val pages: String,
    val perPage: Int,
    val total: String,
    val photo: List<PhotoModel>
)

data class PhotoModel(
    val id: String,
    val title: String,
    val url: String,
    val placeholderRes: Int? = null
)

enum class Size(val suffix: String) {
    MEDIUM_640("z"),
    MEDIUM_800("c"),
    LARGE_1024("b")
}

fun SearchPhotosEntity.toModel() = SearchResultModel(page, pages, perpage, total, photo.map { it.toModel() })

// This templated base url should be placed elsewhere in the case of a real situation. Letting it remain here so it's not hidden away.
// It could be provided as a parameter or or the mapper method could be different.
fun PhotoEntity.toFlickrUrl(size: Size) = "https://live.staticflickr.com/${server}/${id}_${secret}_${size.suffix}.jpg"
fun PhotoEntity.toModel(size: Size = Size.MEDIUM_640) = PhotoModel(id, title, toFlickrUrl(size))