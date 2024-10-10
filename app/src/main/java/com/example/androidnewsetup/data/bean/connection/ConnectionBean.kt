package com.sbm.mobile.app.data.bean.connection

data class ConnectionBean(
    val type: State,
    val isConnected: Boolean = true
) {
    enum class State {
        CELLULAR, WIFI, ETHERNET, NO_INTERNET
    }
}