package com.mayank.ivrrecordapp.ui


import PatientTable
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PermContactCalendar
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.AddPatientRequestBody
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import com.example.ivr_nurse_app.android.UI.Setup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PatientManagement(viewmodel: API_ViewModel ,navHostController: NavHostController )
{
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var screenid by remember {
        mutableStateOf(1)
    }


    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(Constants.light)
            ) {

                Text("Manage Patients", modifier = Modifier.padding(start = 10.dp, top = 30.dp , bottom = 30.dp) , color = Color.Black, fontWeight = FontWeight.Bold , fontStyle = FontStyle.Normal , fontFamily = FontFamily.Default , fontSize = 30.sp)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Add Patients") },
                    icon = { Icon(imageVector = Icons.Filled.PersonAddAlt1, contentDescription = "")},
                    selected = (screenid == 1),
                    onClick = {
                        if (screenid != 1)
                            {
                                screenid = 1
                            }

                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }

                              },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(Constants.light),
                        unselectedTextColor = Color.DarkGray,
                        unselectedIconColor = Color.DarkGray,
                        selectedContainerColor = Color(Constants.dark),
                        selectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
                NavigationDrawerItem(
                    label = { Text(text = "View Patients") },
                    icon = { Icon(imageVector = Icons.Filled.PermContactCalendar, contentDescription = "")},
                    selected = (screenid == 2),
                    onClick = { if (screenid != 2)
                    {
                        screenid = 2

                    }
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                              },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(Constants.light),
                        unselectedTextColor = Color.DarkGray,
                        unselectedIconColor = Color.DarkGray,
                        selectedContainerColor = Color(Constants.dark),
                        selectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
//                NavigationDrawerItem(
//                    label = { Text(text = "Sync Data via SMS") },
//                    icon = { Icon(imageVector = Icons.Filled.Sms, contentDescription = "")},
//                    selected = (screenid == 3),
//                    onClick = { if (screenid != 3)
//                    {
////                        screenid = 3
//                    } },
//                    colors = NavigationDrawerItemDefaults.colors(
//                        unselectedContainerColor = Color.LightGray,//Color(Constants.light),
//                        unselectedTextColor = Color.DarkGray,
//                        unselectedIconColor = Color.DarkGray,
//                        selectedContainerColor = Color(Constants.dark),
//                        selectedTextColor = Color.Black,
//                        selectedIconColor = Color.Black,
//                    ),
//                    shape = RoundedCornerShape(0.dp),
//
//                )
                NavigationDrawerItem(
                    label = { Text(text = "Sync Data via Network") },
                    icon = { Icon(imageVector = Icons.Filled.NetworkCheck, contentDescription = "")},
                    selected = (screenid == 4),
                    onClick = { if (screenid != 4)
                    {
                        screenid = 4
                    }

                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                              },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(Constants.light),
                        unselectedTextColor = Color.DarkGray,
                        unselectedIconColor = Color.DarkGray,
                        selectedContainerColor = Color(Constants.dark),
                        selectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(0.dp)
                )



            }

        }) {
//        patientdatascreen(viewmodel ,navHostController )
        Scaffold(
            containerColor = Color.Transparent,

            floatingActionButton = {
                ExtendedFloatingActionButton(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark),
                    text = { Text("More") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) {
            Crossfade(targetState = screenid) {
                when(it){
                    1 -> patientdatascreen(viewmodel ,navHostController )
                    2 -> PatientTable()
                    else -> Setup(navHostController,viewmodel,0)
                }
            }

        }
    }



}