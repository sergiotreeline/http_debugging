package com.app.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String?,
    @Json(name = "price") val price: Float?,
    @Json(name = "thumbnail") val thumbnail: String?,
    @Json(name = "condition") val condition: String?
)