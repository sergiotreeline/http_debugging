package com.app.remote.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectionChangedBroadcastReceiver(
    private val onReceive: () -> Unit
) : BroadcastReceiver() {

    companion object {
        const val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (isInitialStickyBroadcast) {
            return
        }

        if (intent?.action == CONNECTIVITY_CHANGE_ACTION) {
            onReceive.invoke()
        }
    }
}