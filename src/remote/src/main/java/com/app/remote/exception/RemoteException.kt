package com.app.remote.exception

import com.app.remote.model.ResponseError
import okhttp3.Headers

class RemoteException(val errorCode: Int, val headers: Headers, error: ResponseError?) : Exception()