package com.mayank.ivrrecordapp.FunctionalComponents.NetworkCalls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Display a toast when an SMS is received
        Toast.makeText(context, "SMS Received", Toast.LENGTH_SHORT).show()
    }
}