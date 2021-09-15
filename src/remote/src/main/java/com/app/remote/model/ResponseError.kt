package com.app.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseError(
    @Json(name = "status") val status: String,
    @Json(name = "errorMessage") val errorMessage: String?){

    override fun toString(): String{
        return errorMessage ?: "error"
    }
}