package com.app.demo.ui

import android.net.ConnectivityManager
import androidx.lifecycle.viewModelScope
import com.app.demo.base.BaseAction
import com.app.demo.base.BaseViewModel
import com.app.demo.base.BaseViewState
import com.app.demo.model.ProductItem
import com.app.demo.repository.DataRepository
import com.app.demo.utils.Event
import com.app.demo.utils.event
import com.app.remote.network.ConnectionStateMonitor
import com.app.remote.utils.isConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(private val repository: DataRepository, private val connectivityManager: ConnectivityManager, connectionStateMonitor: ConnectionStateMonitor): BaseViewModel<MainViewModel.MainViewState, MainViewModel.Action>(
    MainViewState(),
    connectionStateMonitor
) {

    data class MainViewState(
        val isLoading: Boolean = false,
        val products: List<ProductItem> = listOf(),
        val error: Event<String>? = null,
        val noInternetConnectionError: Event<Unit>? = null,
    ) : BaseViewState

    sealed class Action : BaseAction {
        class ProductsLoading(val isLoading: Boolean) : Action()
        class ShowProducts(val products: List<ProductItem>) : Action()
        object NoInternetConnection : Action()
        class LoadingProductsError(val error: String?) : Action()
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {

        is Action.ProductsLoading -> state.copy(isLoading = viewAction.isLoading)

        is Action.NoInternetConnection -> state.copy(
            noInternetConnectionError = Unit.event(),
            isLoading = false
        )
        is Action.ShowProducts -> state.copy(
            isLoading = false,
            products = viewAction.products,
            error = null
        )
        is Action.LoadingProductsError -> state.copy(
            isLoading = false,
            error = viewAction.error?.event()
        )
    }

    override fun onNetworkAvailable() {
        getProducts()
    }

    fun getProducts() {

        if(connectivityManager.isConnected){
            viewModelScope.launch {
                sendAction(Action.ProductsLoading(true))
                repository.getProducts()
                    .whenOk {

                        val list = this.valueSafe?.elements?.map { ProductItem(it.id, it.title ?: it.id, "${(it.price ?: 0)} $" ) } ?: listOf()
                        sendAction(Action.ShowProducts(list))
                    }
                    .whenError {
                        sendAction(Action.LoadingProductsError(this.exception?.toString()))
                    }
            }
        }else{
            sendAction(Action.NoInternetConnection)
        }

    }

}