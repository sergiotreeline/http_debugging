package com.app.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsDto(
    @Json(name = "results") val elements: List<ProductDto>?
)