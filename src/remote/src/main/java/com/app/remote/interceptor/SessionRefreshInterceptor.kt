package com.app.remote.interceptor

import android.content.Context
import com.app.remote.ApiClient
import com.app.remote.core.ServiceFactory
import com.app.remote.model.Session
import com.app.remote.utils.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class SessionRefreshInterceptor(context: Context?, private val session: Session) : Interceptor {

    private val mServices: ApiClient = ServiceFactory.createService(
        sessionLocalSource = session,
        appendSessionData = false,
        appendRefreshSessionInterceptor = false,
        context = null
    )

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val initialResponse = chain.proceed(originalRequest)

        return when (initialResponse.code) {
            Constants.StatusCode.UNAUTHORIZED -> {

                session.refreshToken?.let { refreshToken ->
                    val response = runBlocking {
                        mServices.refreshToken(refreshToken = refreshToken)
                    }
                    return when {
                        response.code() != 200 -> {
                            session.clearSession()
                            initialResponse
                        }
                        else -> {
                            response.body()?.data?.let {
                                session.updateSession(it)
                                initialResponse.close()
                                val newAuthenticationRequest = originalRequest.newBuilder()
                                    .header(
                                        "Authorization",
                                        session.accessToken ?: ""
                                    )
                                    .build()
                                chain.proceed(newAuthenticationRequest)
                            } ?: initialResponse
                        }
                    }

                } ?: initialResponse
            }
            else -> initialResponse
        }

    }
}