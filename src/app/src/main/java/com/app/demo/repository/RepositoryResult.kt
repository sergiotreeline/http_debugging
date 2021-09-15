package com.app.demo.repository

sealed class RepositoryRequestStatus {
    object FETCHING : RepositoryRequestStatus()
    object COMPLETE : RepositoryRequestStatus()
    class Error(val error: Exception?) : RepositoryRequestStatus()
}

data class RepositoryResult<T>(
    val data: T?,
    val requestStatus: RepositoryRequestStatus
)
