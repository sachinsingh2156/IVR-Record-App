package com.example.ivr_nurse_app.android.UI



import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Splash(navHostController: NavHostController)
{
    var IconSize by remember {
        mutableStateOf(Animatable(0f))

    }
        val context = LocalContext.current

    var getpermissions = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            if( permissions.values.all { it })
            {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(context,"Permissions Granted",Toast.LENGTH_LONG).show()
                }
            }
        else
            {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(context,"Permissions Not Granted",Toast.LENGTH_LONG).show()
                }
            }
    }

        LaunchedEffect(Unit) {
            val permissions = mutableListOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_NETWORK_STATE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12 (API 31) and above
                permissions.addAll(
                    listOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE
                    )
                )
            } else {
                // Android 11 and below
                permissions.addAll(
                    listOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                    )
                )
            }

// 🚀 Launch the permission request
            getpermissions.launch(permissions.toTypedArray())

        }
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent), contentAlignment = Alignment.Center , )
    {
            Icon(imageVector = Icons.Filled.Description, contentDescription = "",Modifier.size(IconSize.value.dp))
    }
    LaunchedEffect(Unit)
    {
        IconSize.animateTo(targetValue = 100f, animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutLinearInEasing
        ))

        delay(1000)
        navHostController.navigate("managepatients")
    }
}