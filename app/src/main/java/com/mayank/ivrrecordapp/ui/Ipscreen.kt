package com.example.ivr_nurse_app.android.UI

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import com.example.ivr_nurse_app.android.FunctionalComponents.NetworkCalls.CallStateRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@Composable
fun Setup(navHostController: NavHostController, viewModel: API_ViewModel, type: Int)
{
    var context = LocalContext.current
    var sharedPrefs = SharedPrefs(context)
    var status = remember {
        mutableStateOf(0)
    }
    var ipaddress = sharedPrefs.getString("ipaddress","")
    var department = sharedPrefs.getString("department","")

if (type == 0)
{
    Crossfade(targetState = status.value) {
        when(it)
        {
            0 -> Connecting(navHostController = navHostController, ApiViewmodel = viewModel,status)
            1 -> IPscreen(navHostController = navHostController, ApiViewmodel = viewModel,status)
        }
    }
}
    else
{
    IPscreen(navHostController = navHostController, ApiViewmodel = viewModel,status)
}




}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Connecting(
    navHostController: NavHostController,
    ApiViewmodel: API_ViewModel,
    status: MutableState<Int>
)
{
    var screenheight by remember {
        mutableStateOf(androidx.compose.animation.core.Animatable(0f))
    }
    val context = LocalContext.current
    val sharedPrefs = SharedPrefs(context)
    var ipaddress by remember {
        mutableStateOf(sharedPrefs.getString("ipaddress",""))
    }
    var department by remember {
        mutableStateOf(sharedPrefs.getString("department",""))
    }

    LaunchedEffect(Unit) {
        screenheight.animateTo(context.resources.displayMetrics.heightPixels.toFloat(), tween(2000,100,
            FastOutLinearInEasing))
    }

    val connectionMessage by ApiViewmodel.testconnection.collectAsState()
    val errorMessage by ApiViewmodel.errorMessage.collectAsState()
    val loading by ApiViewmodel.loading.collectAsState()
    val databases by ApiViewmodel.databases.collectAsState()


    LaunchedEffect(ipaddress) {

        ipaddress?.let {
            try {
                if (!ipaddress.isNullOrEmpty()) {
                    ApiViewmodel.initialize(ipaddress!!)
                }
                if (!ipaddress.isNullOrEmpty() && !department.isNullOrEmpty()) {
                    // Show Toast on the Main Thread

                    ApiViewmodel.TestConnectionWithBackend()
                } else {
                    status.value = 1 // Update the status if conditions are not met
                }
            } catch (e: Exception) {
                Log.e("Connecting", "Error during initialization: ${e.message}")
                // Optionally, you can set an error message or update the status here
            }
        }

    }





    LaunchedEffect(connectionMessage,databases,department) {

        if (connectionMessage?.message == "Connection Successful" && department != "" && databases != emptyList<String>())
        {
            if(databases.contains(department))
            {
                navHostController.navigate("patientdatascreen")
            }
            else
            {
                status.value = 1
            }
        }

    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenheight.value.dp)
            .padding(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connecting with Server ..",
                modifier = Modifier.padding(top= 60.dp, bottom = 60.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal
            )
            if(loading)
            {
                CustomCircularLoadingIndicator(color = Color.Blue)
            }


            if(errorMessage != "")
            {
                errorMessage?.let {
                    Text(
                        text = "Error: $it",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Button(
                    onClick = {

                        status.value = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(Constants.light),
                        contentColor = Color(Constants.dark)
                    ),
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(text = "Setup Device", fontWeight = FontWeight.Bold)
                }
            }

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPscreen(
    navHostController: NavHostController,
    ApiViewmodel: API_ViewModel,
    status: MutableState<Int>
) {
    val context = LocalContext.current
    val sharedPrefs = SharedPrefs(context)

    var ipaddress by remember {
        mutableStateOf(sharedPrefs.getString("ipaddress", ""))
    }

    var screenheight by remember {
        mutableStateOf(androidx.compose.animation.core.Animatable(0f))
    }

    LaunchedEffect(ipaddress) {
        if (ipaddress!!.isNotEmpty()) {
            ApiViewmodel.initialize(ipaddress!!)



        }
    }

    LaunchedEffect(Unit) {
        screenheight.animateTo(context.resources.displayMetrics.heightPixels.toFloat(), tween(2000,100,
            FastOutLinearInEasing))
    }

    val connectionMessage by ApiViewmodel.testconnection.collectAsState()
    val message by ApiViewmodel.message.collectAsState()

    val errorMessage by ApiViewmodel.errorMessage.collectAsState()
    val databases by ApiViewmodel.databases.collectAsState()
    val loading by ApiViewmodel.loading.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(sharedPrefs.getString("department","Select Department")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenheight.value.dp)
            .padding(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Connect with Server",
                modifier = Modifier.padding(top = 60.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal
            )

            // IP Address Input
            IPAddressInput(ipaddress!!, onValueChange = { ipaddress = it }, sharedPrefs)

            // Dropdown for databases


            // Test Connection Button
            Button(
                onClick = { ApiViewmodel.TestConnectionWithBackend() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(text = "Test Connection", fontWeight = FontWeight.Bold)
            }

            // Connection and Loading Indicators
            ConnectionFeedback(
                connectionMessage = connectionMessage?.message,
                errorMessage = errorMessage,
                loading = loading,

                navHostController,
                message = message,
            )

            Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom if screen is larger

            // Continue Button (If connection successful)

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPAddressInput(ipAddress: String, onValueChange: (String) -> Unit, sharedPrefs: SharedPrefs) {
    OutlinedTextField(
        value = ipAddress,
        onValueChange = {
            onValueChange(it)
            sharedPrefs.add("ipaddress", it)
        },
        placeholder = {
            Text(
                text = "Enter IP Address",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        textStyle = TextStyle(textAlign = TextAlign.Center),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(Constants.dark),
            focusedContainerColor = Color(Constants.light),
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color(Constants.light),
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color(Constants.dark),
            cursorColor = Color(Constants.dark)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}



@Composable
fun ConnectionFeedback(
    connectionMessage: String?,
    errorMessage: String?,
    loading: Boolean,
    navHostController: NavHostController,
    message: String?
) {

    val context = LocalContext.current
    val sharedPrefs = SharedPrefs(context)

    // State to control the visibility of the confirmation dialog
    var showUploadConfirmDialog by remember { mutableStateOf(false) }

    // Confirmation Dialog for Upload
    if (showUploadConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showUploadConfirmDialog = false },
            title = { Text("Confirm Upload") },
            text = { Text("Are you sure you want to upload patient data to the Server?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Original onClick logic for the upload button
                        var patients = CallStateRepository.databaseHelper?.getAllPatients()
                        CoroutineScope(Dispatchers.IO).launch {
                            var issent = async { CallStateRepository.API_viewModel.value?.SendPatientsToBackend() }.await()
                            if (issent!!) {
                                // You might want to show a success message or navigate
                            }
                        }
                        showUploadConfirmDialog = false // Close dialog after action
                    }
                ) {
                    Text("Upload")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showUploadConfirmDialog = false } // Just close the dialog
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        connectionMessage?.let {
            Text(
                text = it,
                color = Color.Blue,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold
            )
        }

        if (loading) {
            CustomCircularLoadingIndicator(color = Color.Blue)
        }

        errorMessage?.let {
            Text(
                text = "$it",
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }

        message?.let {
            Text(
                text = it,
                color = Color.Blue,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold
            )
        }
        connectionMessage?.let { msg -> // Renamed message to msg to avoid conflict
            if (msg == "Connection Successful") {
                Button(
                    onClick = {
                        // Show the confirmation dialog instead of directly executing the upload
                        showUploadConfirmDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(Constants.light),
                        contentColor = Color(Constants.dark)
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Text(text = "Upload", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun CustomCircularLoadingIndicator(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 8f
) {
    val transition = rememberInfiniteTransition()
    // Animation for rotating the circular progress
    val animatedAngle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Canvas to draw the custom circular loading indicator
    Canvas(modifier = modifier.size(50.dp)) {
        val diameter = size.minDimension
        val arcSize = Size(diameter, diameter)

        // Drawing the arc (progress) based on animated angle
        drawArc(
            color = color,
            startAngle = animatedAngle.value,
            sweepAngle = 270f, // This controls how much of the circle is filled (270 degrees for 3/4 circle)
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = arcSize,
            topLeft = Offset((size.width - arcSize.width) / 2, (size.height - arcSize.height) / 2)
        )
    }
}

@Composable
fun SetNewDepartment(ApiViewmodel: API_ViewModel) {

    var context = LocalContext.current

    var department by remember { mutableStateOf("") }
    var message by remember {
        mutableStateOf("")
    }

    val loading by ApiViewmodel.loading.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
//            .imePadding() // Padding when keyboard is visible
            .padding(16.dp) // General padding for the entire box,
        ,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier

                .background(Color.Transparent)
                .verticalScroll(rememberScrollState()), // Scrollable when content overflows
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Title for Message Template
            Text(
                text = "New Department",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(Constants.dark),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Message Input Field
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("Department Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))



            // Set Message Button
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                   CoroutineScope(Dispatchers.IO).launch {
                       message = "Loading..."
                       var request = async { ApiViewmodel.AddDepartmenttoBackend(department) }
                       var responce = request.await()
                       if(responce != null)
                       {
                           message = responce
                       }
                   }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Add Department", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(30.dp))

            Text(text = message, color = Color(Constants.dark), modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), textAlign = TextAlign.Center)



        }
    }
}