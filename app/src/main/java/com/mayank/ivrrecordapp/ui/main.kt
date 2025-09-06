package com.example.ivr_nurse_app.android.UI


import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.UI.Navigation.MyNavhostController



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main()
{


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(

                        Color(Constants.dark),
                        Color(Constants.light),
                        Color(Constants.dark),

                        )
                )
            )
    )
    {
//        Splash()
        MyNavhostController()
        Text(text = "")

//        BLEconnect()
//        Test1()
//        TestBroadcastUI()
    }
}