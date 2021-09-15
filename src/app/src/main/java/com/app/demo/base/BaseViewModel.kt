package com.app.demo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.demo.utils.asLiveData
import com.app.remote.network.ConnectionStateMonitor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

interface BaseViewState
interface BaseAction

open class BaseViewModel<ViewState : BaseViewState, ViewAction : BaseAction> @AssistedInject constructor(@Assisted initialState: ViewState, private val connectionStateMonitor: ConnectionStateMonitor) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.asLiveData()

    // Delegate will handle state event deduplication
    // (multiple states of the same type holding the same data will not be dispatched multiple times to LiveData stream)
    protected var state by Delegates.observable(initialState) { _, old, new ->
        if (old != new) {
            stateMutableLiveData.postValue(new)
        }
    }

    init {
        connectionStateMonitor.enable {
            onNetworkAvailable()
        }
    }

    public override fun onCleared() {
        super.onCleared()
        connectionStateMonitor.disable()
    }

    protected open fun onNetworkAvailable() {

    }

    protected open fun onReduceState(viewAction: ViewAction): ViewState = state

    fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }
}