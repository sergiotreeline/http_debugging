package com.app.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AppendCommonHeadersInterceptor  : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder.header("Accept", "application/json")
        builder.header("Content-Type", "application/json; charset=UTF-8")

        return chain.proceed(builder.build())
    }
}