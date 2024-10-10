package com.example.androidnewsetup.util.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import com.sbm.mobile.app.data.bean.connection.ConnectionBean

class ConnectionHandler(private val context: Context) : LiveData<ConnectionBean>() {

    val connectivityAction = "android.net.conn.CONNECTIVITY_CHANGE"

    override fun onActive() {
        super.onActive()
        val intentFilter = IntentFilter()
        intentFilter.addAction(connectivityAction)
        context.registerReceiver(networkReceiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val actionOfIntent = intent.action
            if (actionOfIntent.equals(connectivityAction)) {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            postValue(ConnectionBean(ConnectionBean.State.CELLULAR, true))
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            postValue(ConnectionBean(ConnectionBean.State.WIFI, true))
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            postValue(ConnectionBean(ConnectionBean.State.ETHERNET, true))
                        }
                    }
                } else {
                    postValue(ConnectionBean(ConnectionBean.State.NO_INTERNET, false))
                }
            }
        }
    }
}