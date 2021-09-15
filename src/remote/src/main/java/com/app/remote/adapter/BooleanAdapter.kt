package com.app.remote.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class EnsuresBoolean


internal class BooleanAdapter {
    @ToJson
    fun toJson(@EnsuresBoolean data: Boolean): Int {
        return when(data) {
            true -> 1
            false -> 0
        }
    }

    @FromJson
    @EnsuresBoolean
    fun fromJson(data: Int): Boolean {
        return when(data) {
            1 -> true
            else -> false
        }
    }
}