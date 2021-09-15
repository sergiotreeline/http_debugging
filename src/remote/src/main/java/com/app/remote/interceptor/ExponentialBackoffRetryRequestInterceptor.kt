package com.app.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.app.remote.utils.isConnected
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.pow
import kotlin.random.Random

abstract class RetryRequestInterceptor : Interceptor

class ExponentialBackoffRetryRequestInterceptor(
    private val context: Context,
    private val maximumBackoffDuration: Double = 30 * 60.0
) : RetryRequestInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // try the request
        var response = chain.proceed(request)

        var retryCount = 1
        var retryDelay = nextTimeDelay(retryCount)

        while (!response.isSuccessful && (response.code > 500 || !isConnected) && retryDelay < maximumBackoffDuration) {
            Thread.sleep(retryDelay.toLong() * 1000)

            // retry the request
            response.close() // https://github.com/square/okhttp/issues/4986#issuecomment-533181869
            response = chain.proceed(request)

            retryCount++
            retryDelay = nextTimeDelay(retryCount)
        }

        // otherwise just pass the original response on
        return response
    }

    private fun nextTimeDelay(retryCount: Int): Double {
        val baseTime = 2.0.pow(retryCount.toDouble())
        val randomDelay = Random.nextDouble(baseTime / 2.0)
        return baseTime + randomDelay
    }

    private val isConnected: Boolean
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.isConnected
        }
}
