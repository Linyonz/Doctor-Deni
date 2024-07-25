package com.example.doctordeni.ui.screens.tracking

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.requests.LoanData
import com.example.doctordeni.models.responses.LoansProvidersItem
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.DeniDate
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanDetail(navHostController: NavHostController? = null) {

    val context = LocalContext.current
    var loanDate by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var loanAmount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(LoansProvidersItem())
    }

    val trackerViewModel = koinViewModel<TrackerViewModel>()
    val providers = trackerViewModel.loanProviders.collectAsState()
    val serviceProvidersList = providers.value?.toList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        item {
            Text(
                text = "Add A loan",
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            DeniDate(
                dateTitle = "Loan Date", id = 3
            ) { year, month, day ->
                val paddedDay = padDigits(day)
                val paddedMonth = padDigits(month+1)
                loanDate = "$year-$paddedMonth-$paddedDay"
            }
        }

        item {
            DeniDate(
                dateTitle = "Due Date", id = 4
            ) { year, month, day ->
                val paddedDay = padDigits(day)
                val paddedMonth = padDigits(month+1)
                dueDate = "$year-$paddedMonth-$paddedDay"
            }
        }


        item {
            OutlinedTextField(value = loanAmount,
                onValueChange = { loanAmount = it },
                label = { Text("Loan Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )



            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedItem.name ?: "selected Item",
                    onValueChange = {},
                    label = { Text(text = "Service Provider") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    serviceProvidersList?.forEach { option: LoansProvidersItem ->
                        DropdownMenuItem(text = { Text(text = option.name ?: "service provider") },
                            onClick = {
                                expanded = false
                                selectedItem = option
                            })
                    }
                }
            }

        }

        item {
            Button(onClick = {
                if (loanDate.isBlank()) {
                    Toast.makeText(context, "Please select a loan date", Toast.LENGTH_SHORT).show()
                }
                if (dueDate.isBlank()) {

                    Toast.makeText(context, "Please select a due date", Toast.LENGTH_SHORT).show()
                }
                if (loanAmount.isBlank()) {
                    Toast.makeText(context, "Please enter a loan amount", Toast.LENGTH_SHORT).show()
                }
                if (selectedItem.name.isNullOrBlank()) {
                    Toast.makeText(context, "Please select a service provider", Toast.LENGTH_SHORT)
                        .show()
                }

                if (loanDate.isNotBlank() && dueDate.isNotBlank() && loanAmount.isNotBlank() && selectedItem.name?.isNotBlank()!!) {


                    val loanD = LoanData(
                        loanId = UUID.randomUUID().toString(),
                        loanDate = loanDate,
                        loanDueDate = dueDate,
                        loanAmount = loanAmount.toLong(),
                        serviceProvider = selectedItem.name,
                        interestRate = selectedItem.interestRate?.toFloat()
                    )
                    trackerViewModel.addLoanData(loanD)
                    navHostController?.navigate(BottomNavigation.Track.route)
                }
            }) {
                Text(text = "Add Loan")
            }
        }

    }
}

fun padDigits(month: Int): String {
    return String.format(Locale.getDefault(), "%02d", month)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun LoanDetailPreview() {
    DoctorDeniTheme {
        LoanDetail()
    }
}