package com.mayank.ivrrecordapp.ui

import android.content.Context
import android.provider.Settings.Secure
import java.text.SimpleDateFormat
import java.util.*

fun generateUniqueID(context: Context): String {
    // Get the device's unique identifier (IMEI or Android ID)
    val deviceId = getDeviceIdentifier(context)

    // Get the current date and time
    val currentTime = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())

    // Combine the device ID and current time to create a unique ID
    val uniqueID = "$deviceId$currentTime"

    return uniqueID
}

fun getDeviceIdentifier(context: Context): String {
    // Get the Android ID as a device identifier
    val androidID = Secure.getString(context.contentResolver, Secure.ANDROID_ID)

    // You can also use IMEI if necessary, but be sure to request appropriate permissions.
    // val imei = getIMEI(context)

    return androidID ?: "UNKNOWN"
}

// Request necessary permissions and implement getIMEI if you want to use IMEI
// fun getIMEI(context: Context): String {
//     val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//     return telephonyManager.imei ?: "UNKNOWN"
// }

// Usage

