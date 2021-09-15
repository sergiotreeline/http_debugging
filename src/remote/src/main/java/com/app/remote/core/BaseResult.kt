package com.app.remote.core

import com.app.remote.exception.RemoteException
import com.app.remote.model.ResponseError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import okhttp3.Headers
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Wrapper class for passing data between repositories and viewmodels
 */
@Suppress("unused")
sealed class BaseResult<out T : Any> {

    class Ok<out T : Any>(private val _value: T?) : BaseResult<T>() {
        val value: T
            get() = _value!!

        val valueSafe: T?
            get() = _value
    }

    class CancelledError : Error()
    class ApiError(val code: Int, val headers: Headers, exception: Exception? = null) :
        Error(exception)

    class NetworkError(exception: Exception? = null) : Error(exception)
    open class Error(val exception: Exception? = null) : BaseResult<Nothing>()

    @PublishedApi
    internal var isResultHandled = false

    fun forceGet(): T {
        if (this is Ok) {
            return this.value
        } else {
            throw ((this as Error).exception as Throwable)
        }
    }

    fun safeGet(): T? = if (this is Ok) {
        this.valueSafe
    } else {
        null
    }

    inline fun whenOk(block: Ok<T>.() -> Unit): BaseResult<T> {
        if (this is Ok) {
            this.block()
            isResultHandled = true
        }
        return this
    }

    inline fun whenNetworkError(block: NetworkError.() -> Unit): BaseResult<T> {
        if (!isResultHandled && this is NetworkError) {
            this.block()
            isResultHandled = true
        }
        return this
    }

    inline fun whenApiError(errorCode: Int, block: ApiError.() -> Unit): BaseResult<T> {
        if (!isResultHandled && this is ApiError && this.code == errorCode) {
            block()
            isResultHandled = true
        }
        return this
    }

    inline fun whenApiError(block: ApiError.() -> Unit): BaseResult<T> {
        if (!isResultHandled && this is ApiError) {
            block()
            isResultHandled = true
        }
        return this
    }

    inline fun whenError(block: Error.() -> Unit): BaseResult<T> {
        if (!isResultHandled && this is Error && this !is CancelledError) {
            this.block()
            isResultHandled = true
        }
        return this
    }
}

suspend fun <T : Any> getResult(sourceCall: suspend CoroutineScope.() -> T?) = coroutineScope {
    try {
        BaseResult.Ok(sourceCall(this))
    } catch (exception: CancellationException) { // Coroutine can be cancelled
        BaseResult.CancelledError()
    } catch (exception: RemoteException) {
        BaseResult.ApiError(
            exception = exception,
            headers = exception.headers,
            code = exception.errorCode
        )
    } catch (exception: UnknownHostException) {
        BaseResult.NetworkError(exception = exception)
    } catch (exception: ConnectException) {
        BaseResult.NetworkError(exception = exception)
    } catch (exception: SocketTimeoutException) {
        BaseResult.NetworkError(exception = exception)
    } catch (exception: Exception) {
        exception.printStackTrace()
        BaseResult.Error(exception = exception)
    }
}

fun <T : Any> Response<T>.bodyOrThrow() = if (isSuccessful) {
    body() ?: throw IOException("Body can't be null")
} else {
    val obj = errorBody()?.string()?.let { json ->
        val moshi = ServiceFactory.moshi()
        val parser = moshi.adapter(ResponseError::class.java)
        try {
            parser.fromJson(json)
        } catch (e: Exception) {
            null
        }
    }
    throw RemoteException(code(), headers(), error = obj)
}