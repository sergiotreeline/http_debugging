package com.app.remote.core

import android.content.Context
import com.app.remote.BuildConfig
import com.app.remote.adapter.BooleanAdapter
import com.app.remote.interceptor.AuthenticationInterceptor
import com.app.remote.interceptor.ExponentialBackoffRetryRequestInterceptor
import com.app.remote.interceptor.SessionRefreshInterceptor
import com.app.remote.model.Session
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    inline fun <reified T> createService(
        addRetryRequestInterceptor: Boolean = false,
        converterFactory: Converter.Factory = MoshiConverterFactory.create(moshi()),
        context: Context? = null
    ): T =
        retrofit(
            baseUrl = BuildConfig.HOST,
            sessionLocalSource = null,
            appendSessionData = false,
            appendRefreshSessionInterceptor = false,
            addRetryRequestInterceptor = addRetryRequestInterceptor,
            converterFactory = converterFactory,
            context = context
        ).create(T::class.java)

    inline fun <reified T> createService(
        sessionLocalSource: Session,
        addRetryRequestInterceptor: Boolean = false,
        appendSessionData: Boolean = true,
        appendRefreshSessionInterceptor: Boolean = true,
        converterFactory: Converter.Factory = MoshiConverterFactory.create(moshi()),
        context: Context? = null
    ): T =
        retrofit(
            baseUrl = BuildConfig.HOST,
            sessionLocalSource = sessionLocalSource,
            appendSessionData = appendSessionData,
            appendRefreshSessionInterceptor = appendRefreshSessionInterceptor,
            addRetryRequestInterceptor = addRetryRequestInterceptor,
            converterFactory = converterFactory,
            context = context
        ).create(T::class.java)

    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(BooleanAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    fun retrofit(
        baseUrl: String,
        sessionLocalSource: Session?,
        appendSessionData: Boolean,
        appendRefreshSessionInterceptor: Boolean,
        addRetryRequestInterceptor: Boolean,
        converterFactory: Converter.Factory,
        context: Context?
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .client(
            okHttp(
                sessionLocalSource,
                appendSessionData = appendSessionData,
                appendRefreshSessionInterceptor = appendRefreshSessionInterceptor,
                addRetryRequestInterceptor = addRetryRequestInterceptor,
                context = context
            )
        )
        .build()

    private fun okHttp(
        sessionLocalSource: Session?,
        appendSessionData: Boolean = true,
        appendRefreshSessionInterceptor: Boolean,
        addRetryRequestInterceptor: Boolean,
        context: Context?
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .apply {
                if (addRetryRequestInterceptor && context != null) {
                    addInterceptor(ExponentialBackoffRetryRequestInterceptor(context))
                }
            }

        if (appendRefreshSessionInterceptor) {
            sessionLocalSource?.apply {
                builder.addInterceptor(SessionRefreshInterceptor(context, this))
            }
        }

        if (appendSessionData) {
            sessionLocalSource?.apply {
                builder.addInterceptor(AuthenticationInterceptor(this))
            }
        }

        return builder.build()
    }

}