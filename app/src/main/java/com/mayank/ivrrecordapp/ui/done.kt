//package com.mayank.ivrrecordapp.ui
//
//import android.widget.Toast
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.layout.BoxScopeInstance.matchParentSize
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Popup
//import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
//import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import viewmodel
//
//@Composable
//fun pastpatients(viewmodel: API_ViewModel) {
//
//    var patientid by remember {
//        mutableStateOf("")
//
//    }
//
//    var showDialog by remember { mutableStateOf(false) }
//
//    var pdata by remember {
//        mutableStateOf(emptyList<Patient>())
//    }
//    var dataToRemove by remember {
//        mutableStateOf(emptyList<String>())
//    }
//    var context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        contentAlignment = Alignment.TopCenter
//    )
//    {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier
//                .padding(top = 10.dp)
////                .verticalScroll(
////                    rememberScrollState()
////                )
//        ) {
//            Text(
//                text = "Remove Post-Operated Patients",
//                fontFamily = FontFamily.Default,
//                fontStyle = FontStyle.Normal,
//                fontSize = 30.sp,
//                fontWeight = FontWeight.ExtraBold,
//                color = Color.Black,
//                modifier = Modifier.padding(top = 20.dp),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(25.dp))
//            OutlinedTextField(
//                value = patientid,
//                onValueChange = { patientid = it },
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Green,
//                    focusedTextColor = Color.Black
//
//                ),
//                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
//                label = { Text(text = "PATIENTID ID") },
//                placeholder = { Text(text = "Enter PID") }
//            )
//            Spacer(modifier = Modifier.height(15.dp))
//            if(patientid.isNotBlank())
//            {
//                Row(modifier = Modifier.background(Color.Transparent), horizontalArrangement = Arrangement.SpaceBetween){
//                    Button(modifier = Modifier
//                        .weight(1f)
//                        .wrapContentWidth(), onClick = {
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            try {
//                                var response = viewmodel.databyid(patientid)
////
//                                pdata = response
//                                if(pdata == emptyList<Patient   >())
//                                {
//                                    GlobalScope.launch(Dispatchers.Main){
//                                        Toast.makeText(
//                                            context,
//                                            "No data for patientID : ${patientid}",
//                                            Toast.LENGTH_SHORT
//                                        )
//                                            .show()
//                                    }
//                                }
//                            } catch (e: Exception) {
//                                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//
//                        }
//
//
//                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
//                        Text(text = "Fetch", color = Color.Black)
//                    }
//                    if(dataToRemove != emptyList<patientdata>())
//                    {
//                        Spacer(modifier = Modifier.width(3.dp))
//                        Button(modifier = Modifier
//                            .weight(1f)
//                            .wrapContentWidth(),onClick = {
//                            showDialog = true
//                             }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
//                            Text(text = "Remove", color = Color.Black)
//                        }
//
//                        if(showDialog)
//                        {
//                            AlertDialog(modifier = Modifier.background(Color.White), onDismissRequest = { showDialog = false },
//                            title = { Text(text = "Delete Confirmation", color = Color.Magenta)},
//                            text = { Text(text = "Are u sure ?",color = Color.Black)},
//                            confirmButton = { Button(onClick = {
//
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    for ( i in dataToRemove){
//                                        try {
//                                            var response = viewmodel.delete(i)
//                                            pdata = viewmodel.databyid(patientid)
//
//                                        } catch (e: Exception) {
//                                            Toast.makeText(
//                                                context,
//                                                e.message.toString(),
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//                                    dataToRemove = emptyList()
//
//                                }
//                            },
//                            colors =  ButtonDefaults.buttonColors(backgroundColor = Color.Magenta, contentColor = Color.White)) {
//                                Text(text = "Yes")
//                            }},
//                            dismissButton = { Button(onClick = { showDialog = false },
//                                colors =  ButtonDefaults.buttonColors(backgroundColor = Color.Magenta, contentColor = Color.White)) {
//                                Text(text = "No")
//                            }})
//                        }
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            LazyColumn()
//            {
//                items(pdata)
//                {
//                     mydata ->
//                        var bcolor by remember {
//                            mutableStateOf(Color.Green)
//                        }
//                        var isselected by remember {
//                            mutableStateOf("Select")
//                        }
//                        patientdatacard(data = mydata, bcolor = bcolor, isselected = isselected)
//                        {
//                            if (isselected == "Select") {
//                                bcolor = Color.Red
//                                isselected = "Unselected"
//                                dataToRemove = dataToRemove + listOf(mydata.engagementid)
//                            } else {
//                                bcolor = Color.Green
//                                isselected = "Select"
//                                dataToRemove = dataToRemove.filter { it != mydata.engagementid }
//                            }
//                        }
//
//                }
//            }
//        }
//
//    }
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
//        Button(onClick = { myfunction() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
//            Text(text = "Add Patients", color = Color.Black)
//        }
//    }
//}
//
//
//@Composable
//fun patientdatacard(data: patientdata, bcolor: Color,  isselected: String , function: () -> Unit,) {
//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    Card(modifier = Modifier
//        .padding(10.dp)
//        .fillMaxWidth()
//        .wrapContentHeight()
//        .animateContentSize()
//        .clickable { expanded = !expanded }, backgroundColor = bcolor, shape = RoundedCornerShape(20.dp))
//    {
//        Column(modifier = Modifier
//            .padding(10.dp)
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .background(Color.Transparent)){
//
//
//            Text(text = "EID : ${data.engagementid}")
//
//            Spacer(modifier = Modifier.height(10.dp))
//            if(expanded)
//            {
//                Text(text ="OperationType : ${data.operationtype}" )
//                Spacer(modifier = Modifier.height(5.dp))
//                Text(text ="Duedate : ${data.duedate}")
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = function, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = bcolor)) {
//                    Text(text = isselected )
//                }
//
//
//
//            }
//        }
//    }
//}