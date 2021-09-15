package com.app.remote.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

open class ConnectionStateMonitor(private val context: Context) : ConnectivityManager.NetworkCallback() {

    private var onNetworkAvailable = {}
    private var connectivityManager: ConnectivityManager? = null

    private val networkRequest: NetworkRequest by lazy {
        NetworkRequest.Builder().addTransportType(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
    }

    open fun enable(onNetworkAvailable: () -> Unit = {}) {
        this.onNetworkAvailable = onNetworkAvailable

        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.registerNetworkCallback(networkRequest, this)
    }

    open fun disable() {
        connectivityManager?.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        onNetworkAvailable()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
    }
}