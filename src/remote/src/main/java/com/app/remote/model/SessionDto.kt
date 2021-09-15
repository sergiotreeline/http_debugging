package com.app.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionDto(

    @Json(name = "accessToken")
    var accessToken: String,

    @Json(name = "refreshToken")
    var refreshToken: String,

    @Json(name = "tokenType")
    val type: String

)

@JsonClass(generateAdapter = true)
data class SessionResponseDto(

    @Json(name = "status")
    val status: String,

    @Json(name = "errorMessage")
    val errorMessage: String?,

    @Json(name = "data")
    val data: SessionDto?

)// : ApiResponse()