package com.example.androidnewsetup.util.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.androidnewsetup.data.Constants
import com.example.androidnewsetup.util.broadcast.CustomBroadcastInterface

class CustomBroadcastReceiver(private var listener: CustomBroadcastInterface?) :
    BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id: Int = intent.getIntExtra(Constants.Broadcast.ID, 0)
        Log.d("receiver", "Got message: $id")

        listener?.onDataReceive(id)
    }
}