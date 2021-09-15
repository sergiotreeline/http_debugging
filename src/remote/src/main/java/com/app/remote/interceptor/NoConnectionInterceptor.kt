package com.app.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.app.remote.exception.NoConnectivityException
import com.app.remote.utils.isConnected
import okhttp3.Interceptor
import okhttp3.Response


class NoConnectionInterceptor constructor(val context: Context) : Interceptor {

    private var connectivityManager: ConnectivityManager? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        return if (connectivityManager?.isConnected == false) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }
}