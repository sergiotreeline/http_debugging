package com.app.remote.model

interface Session{
    var accessToken: String?
    var refreshToken: String?
    fun updateSession(session: SessionDto)
    fun clearSession()
    fun hasSession(): Boolean
}