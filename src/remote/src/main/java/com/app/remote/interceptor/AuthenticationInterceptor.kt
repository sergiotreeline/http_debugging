package com.app.remote.interceptor

import com.app.remote.model.Session
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor constructor(private val session: Session) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        session.accessToken?.let {
            builder.header("Authorization", it)
        }

        return chain.proceed(builder.build())
    }
}