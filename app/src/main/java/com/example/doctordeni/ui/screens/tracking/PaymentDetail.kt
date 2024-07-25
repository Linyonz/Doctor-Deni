package com.example.doctordeni.ui.screens.tracking

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.requests.PaymentData
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.DeniDate
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetail(navHostController: NavHostController? = null){

    val trackerViewModel = koinViewModel<TrackerViewModel>()

    val repaymentDatePickerState: DatePickerState = rememberDatePickerState()
    repaymentDatePickerState.displayMode = DisplayMode.Picker
    var repaymentDate by remember { mutableStateOf("") }
    var repaymentAmount by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        item {
            Text(
                text = "Add A Payment",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp,bottom = 16.dp)
            )
        }
        item {
           DeniDate(
               dateTitle = "Payment Date"
           ) { year, month, day ->
               val paddedDay = padDigits(day)
               val paddedMonth = padDigits(month+1)
               repaymentDate = "$year-$paddedMonth-$paddedDay"

           }
        }
        item {
            OutlinedTextField(
                value = repaymentAmount,
                onValueChange = { repaymentAmount = it.trim() },
                label = { Text("Repayment Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )

            )
        }
        item {
            OutlinedTextField(
                value = transactionId,
                onValueChange = { transactionId = it.trim() },
                label = { Text("Transaction Id") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )

            )
        }

        item {
            Button(onClick = {
                val payment = PaymentData(
                    repaymentDate = repaymentDate,
                    repaymentAmount = repaymentAmount.toLong(),
                    transactionId = transactionId
                )
                trackerViewModel.addPaymentData(payment)
                navHostController?.navigate(BottomNavigation.Track.route)
            }) {
                Text(text = "Pay")
            }
        }

    }

}


@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PaymentDetailPreview(){
    DoctorDeniTheme {
        PaymentDetail()
    }
}