package com.mayank.ivrrecordapp.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.AlertDialog
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
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import com.example.ivr_nurse_app.android.FunctionalComponents.NetworkCalls.CallStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date



@Composable
fun patientdatascreen(viewmodel: API_ViewModel ,navHostController: NavHostController ) {
    var context = LocalContext.current
    var sharedPrefs = SharedPrefs(context)
    var departmentname by remember { mutableStateOf("") }
    var eid by remember { mutableStateOf(generateUniqueID(context)) }
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cno by remember { mutableStateOf("") }
    var optype by remember { mutableStateOf("") }
    var lang by remember { mutableStateOf("") }
    var duedays by remember { mutableStateOf("") }
    var duedate_1 = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    fun senddata() {
        if (eid.isNotBlank() && id.isNotBlank() && name.isNotBlank() && cno.isNotBlank() && optype.isNotBlank() && lang.isNotBlank() && duedate_1.value.isNotBlank() && departmentname.isNotBlank() ) {

                CoroutineScope(Dispatchers.IO).launch {
                    var duedate = duedate_1.value
                    Log.i("duedate",duedate)
                    var requestbody = Patient(
                        eid.toLowerCase(),
                        id.toLowerCase(),
                        name,
                        cno.trim(),
                        optype.toLowerCase().trim(),
                        lang.toLowerCase().trim(),

                        departmentname,
                        "",
                        0,
                        duedate,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    )
                    var deferredresponce = async { CallStateRepository.databaseHelper!!.insertPatient(requestbody) }
                    var isstored = deferredresponce.await()
                    if (isstored) {
                        MainScope().launch {
                            Toast.makeText(context,"Data Stored in Device Successfully", Toast.LENGTH_LONG).show()


                                 id = ""
                                 name = ""
                                 cno = ""
                                 optype = ""
                                 lang = ""
                                 duedays = ""
                                departmentname = ""
                                 duedate_1.value = ""

                        }
                    }
                }

        } else {
            Toast.makeText(context, "Fields Empty", Toast.LENGTH_LONG).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // Dismiss when clicked outside
            title = { Text("Confirm Add") },
            text = { Text("Are you sure you want to add this patient data?") },
            confirmButton = {
                Button(
                    onClick = {
                        senddata()
                        showDialog = false // Close dialog after action
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false } // Just close the dialog
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = "Patient Data",
                fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Normal,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Blue.copy(alpha = 0.3f),
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                label = { Text(text = "Patient ID") },
                placeholder = { Text(text = "Enter Patient ID") }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                label = { Text(text = "Patient Name") },
                placeholder = { Text(text = "Enter Patient Name") }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = cno,
                onValueChange = { cno = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                label = { Text(text = "Contact No.") },
                placeholder = { Text(text = "Enter Contact No.") }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = optype,
                onValueChange = { optype = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                label = { Text(text = "Operation Type") },
                placeholder = { Text(text = "Enter Operation Type") }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = lang,
                onValueChange = { lang = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                label = { Text(text = "Patient's Language") },
                placeholder = { Text(text = "Enter Language") }
            )

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = departmentname,
                onValueChange = { departmentname = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light)
                ),
                label = { Text(text = "Department") },
                placeholder = { Text(text = "Enter Department") }
            )

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier.clickable { showDatePicker(context, duedate_1) },
                readOnly = true,
                value = duedate_1.value,
                onValueChange = { duedate_1.value = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(Constants.dark),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(Constants.dark),
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedContainerColor = Color(Constants.dark).copy(alpha = 0.4f),
                    focusedContainerColor = Color(Constants.light),
                    focusedTrailingIconColor = Color(Constants.dark),
                    unfocusedTrailingIconColor = Color(Constants.light)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text(text = "Due Date -> Implant Removal") },
                placeholder = { Text(text = "Enter Due Date") },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "", modifier = Modifier.clickable {
                        showDatePicker(context, duedate_1)
                    })
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = {

//                senddata()
//                Toast.makeText(context,"Data Stored in Device Successfully", Toast.LENGTH_LONG).show()
                showDialog = true

            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(Constants.light),
                contentColor = Color(Constants.dark)
            ), border = BorderStroke(3.dp, Color(Constants.dark)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Add")
            }
        }
    }

//    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceAround) {
//        Button(onClick = {  }, colors = ButtonDefaults.buttonColors(containerColor = Color(Constants.light), contentColor = Color.Black)) {
//            Text(text = "Remove Patients", color = Color.Black)
//        }
//        Button(onClick = { navHostController.navigate("manualsetup") }, colors = ButtonDefaults.buttonColors(containerColor = Color(Constants.light), contentColor = Color.Black)) {
//            Text(text = "Setup", color = Color.Black)
//        }
//    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun getdate(days: String):String
{
    var date = LocalDate.now().plusDays(days.toLong())
    var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return date.format(formatter)
}

fun showDatePicker(context: Context, duedate_1: MutableState<String>)
{
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val formattedDate = String.format("%02d-%02d-%04d", mDayOfMonth, mMonth + 1, mYear)
            duedate_1.value = formattedDate
        }, mYear, mMonth, mDay
    )

    mDatePickerDialog.show()

}