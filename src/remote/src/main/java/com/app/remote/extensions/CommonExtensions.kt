package com.app.remote.extensions

import com.app.remote.BuildConfig
import okhttp3.Protocol
import okhttp3.Request

typealias Ignored = Unit

val Any.CLASS_TAG
    get() = this::class.java.toString()

fun okhttp3.Response.Builder.buildSuccess(): okhttp3.Response {
    val mockRequest = Request.Builder().url(BuildConfig.HOST).build()
    request(mockRequest)
    code(200)
    message("OK!")
    protocol(Protocol.HTTP_1_1)
    return build()
}