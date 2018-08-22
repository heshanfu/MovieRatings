package com.fenchtose.movieratings.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name="Search")
    val results: List<Movie> = listOf(),

    @Json(name="totalResults")
    val total: Int = 0,

    @Json(name="Response")
    val success: Boolean = false,

    @Json(name="Error")
    val error: String = ""
)