package com.app.demo.repository

import android.content.Context
import com.app.remote.ApiClient
import com.app.remote.core.BaseResult
import com.app.remote.core.bodyOrThrow
import com.app.remote.core.getResult
import com.app.remote.model.ProductsDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DataRepository @Inject constructor(
    private val services: ApiClient,
    @ApplicationContext val context: Context
) {

    private var fetchMobileConfigJob: Job? = null
    private val mProductStateFlow = MutableStateFlow<RepositoryResult<ProductsDto>?>(null)
    val productStateFlow: StateFlow<RepositoryResult<ProductsDto>?> = mProductStateFlow

    suspend fun getProducts1() = withContext(Dispatchers.IO) {
        mProductStateFlow.value = RepositoryResult(null, RepositoryRequestStatus.FETCHING)
        fetchMobileConfigJob?.cancel()
        fetchMobileConfigJob = launch {
            getResult {
                services.search("juguetes").bodyOrThrow()

            }.whenOk {
                mProductStateFlow.value = RepositoryResult(this.value, RepositoryRequestStatus.COMPLETE)
            }.whenError {
                mProductStateFlow.value = RepositoryResult(null, RepositoryRequestStatus.Error(this.exception))
            }
        }
    }

    suspend fun getProducts(): BaseResult<ProductsDto> = getResult{
        services.search("juguetes").bodyOrThrow()
    }

}