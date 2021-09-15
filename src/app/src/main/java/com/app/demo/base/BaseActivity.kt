package com.app.demo.base

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.app.demo.R
import com.app.demo.utils.Event
import com.app.remote.extensions.Ignored
import com.app.remote.network.ConnectionChangedBroadcastReceiver
import com.app.remote.utils.isConnected

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity @Inject constructor(): AppCompatActivity() {

    open val viewModel: BaseViewModel<*, *>? = null


    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private val connectionChangedBroadcastReceiver = ConnectionChangedBroadcastReceiver() {
        if (!connectivityManager.isConnected) {
            showNoConnectionAlert()
        }
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(
            connectionChangedBroadcastReceiver,
            IntentFilter(ConnectionChangedBroadcastReceiver.CONNECTIVITY_CHANGE_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(connectionChangedBroadcastReceiver)
    }


    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(this@BaseActivity, { onChanged(it) })
    }

    fun <T> LiveData<Event<T>>.observeNotHandled(onChanged: (T) -> Unit) {
        observe(this@BaseActivity, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                onChanged(it)
            }
        })
    }

    protected fun showNoConnectionAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.error_no_internet_connection_title)
            .setMessage(R.string.error_no_internet_connection_message)
            .setPositiveButton(R.string.settings) { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton(R.string.later_capitalized) { _, _ -> Ignored }
            .show()
    }

    fun showErrorDialog(title: String, message: String): AlertDialog {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }

        return dialog.show()
    }

}
