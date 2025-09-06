import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
import com.example.ivr_nurse_app.android.FunctionalComponents.NetworkCalls.CallStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTable() {
    var context = LocalContext.current
    var patients by remember { mutableStateOf(emptyList<Patient>()) }
    // State to control the visibility of the delete confirmation dialog
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    // State to store the engagementId of the patient to be deleted
    var patientToDeleteEngagementId by remember { mutableStateOf<String?>(null) }

    // It's generally better to fetch initial data within a LaunchedEffect
    // to avoid recompositions triggering the fetch repeatedly.
    // However, if CallStateRepository.databaseHelper?.getAllPatients() is not a suspend function
    // and returns the data directly, your current approach is okay.
    // For this example, I'll keep your existing data fetching logic.
    val deferredPatients = CallStateRepository.databaseHelper?.getAllPatients()
    if (deferredPatients != null) {
        patients = deferredPatients
    }

    fun deletePatientAndUpdateList(engagementid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val isDeleted =
                async { CallStateRepository.databaseHelper?.deletePatientByEngagementId(engagementid) }.await()
            if (isDeleted == true) { // Explicitly check for true
                val newPatientList = CallStateRepository.databaseHelper?.getAllPatients()
                if (newPatientList != null) {
                    // Update the state on the Main thread as it affects UI
                    withContext(Dispatchers.Main) {
                        patients = newPatientList
                        Toast.makeText(context, "Patient deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Unable to Delete Data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog && patientToDeleteEngagementId != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                patientToDeleteEngagementId = null // Reset when dialog is dismissed
            },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this patient?") },
            confirmButton = {
                Button(
                    onClick = {
                        patientToDeleteEngagementId?.let { engagementId ->
                            deletePatientAndUpdateList(engagementId)
                        }
                        showDeleteConfirmDialog = false
                        patientToDeleteEngagementId = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        patientToDeleteEngagementId = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Patients List",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            // Headings Row
            item {
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ... your Text headers ...
                        Text(
                            "Patient ID",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(
                                    BorderStroke(1.dp, Color.Gray)
                                )
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Patient Name",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Contact Number",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Operation Type",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Language",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Department",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Due Date",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Action",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Divider()
                }
            }

            // Patient Data Rows
            items(
                patients,
                key = { patient -> patient.engagementId }) { patient -> // Added a key for better performance
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ... your patient data Text composables ...
                        Text(
                            patient.patientId, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.patientName, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.patientCno, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.operationType, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.language, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.department, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            patient.dueDate, modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp), textAlign = TextAlign.Center
                        )

                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Patient", // Added content description
                            tint = Color.Red,
                            modifier = Modifier
                                .width(200.dp)
                                .border(BorderStroke(1.dp, Color.Gray))
                                .padding(10.dp)
                                .clickable {
                                    // Set the patient to delete and show the dialog
                                    patientToDeleteEngagementId = patient.engagementId
                                    showDeleteConfirmDialog = true
                                }
                        )
                    }
                }
                Divider()
            }
        }
    }
}

