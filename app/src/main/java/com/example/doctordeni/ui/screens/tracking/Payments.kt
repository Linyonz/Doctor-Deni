package com.example.doctordeni.ui.screens.tracking

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.states.ScreenStateEnum
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.ui.components.StateScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode.formatNumberWithCommas
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Payments(navigationHostController: NavHostController? = null) {

    val trackerViewModel = koinViewModel<TrackerViewModel>()
    val repayList = trackerViewModel.payments.collectAsState()
    val loans = trackerViewModel.loans.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
    ) { padding ->
        if (repayList.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                if(loans.value.isNotEmpty()){
                   loans.value.forEach {
                       stickyHeader {
                           LoanItemCard(
                               provider = it.serviceProvider ?: "Provider",
                               loanAmount = it.loanAmount?: 0L,
                               amountPaid = it.amountPaid ?: 0L,
                               loanBalance = it.loanAmount?.minus(it.amountPaid?: 0L) ?: 0L,
                               onClickPayment = {
                                   trackerViewModel.selectLoan(it)
                                   navigationHostController?.navigate(BaseNav.PaymentDetail.route)
                               }
                           )
                       }

                       items(trackerViewModel.getPaymentsByLoanId()[it.loanId]?: emptyList()){ payment ->
                           PaymentCard(
                               repaymentDate = payment.repaymentDate,
                               repaymentAmount = payment.repaymentAmount ?: 0L,
                               transactionId = payment.transactionId!!,
                               loanId = it.loanId!!
                           )
                       }
                   }
                }
//                items(repayList.value) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 16.dp),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(modifier = Modifier.fillMaxWidth()) {
//
//                            PaymentCard(
//                                repaymentDate = it.repaymentDate,
//                                repaymentAmount = it.repaymentAmount.toString(),
//                                transactionId = it.transactionId!!,
//                                loanId = it.loanId!!
//                            )
//                        }
//                    }
//                }
            }
        }

        if (repayList.value.isEmpty()) {
            StateScreen(screenStateEnum = ScreenStateEnum.EMPTY)
        }
    }
}

@Composable
fun PaymentCard(
    repaymentDate:String? = "01/01/2000",
    repaymentAmount:Long = 0L,
    transactionId:String = "TransID",
    loanId:String = "loanId"
){
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(
                    imageVector = Icons.Filled.MonetizationOn,
                    contentDescription = "repayment icon"
                )
            }
            Column(
                modifier = Modifier.weight(4f)
            ){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    
                ){
                    Text(
                        "Repayment Date: ",
                        fontWeight= FontWeight.Bold
                    )
                    Text(
                        repaymentDate!!,
                        fontWeight= FontWeight.Light
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),

                ){
                    Text(
                        "Repayment Amount: ",
                        fontWeight= FontWeight.Bold
                    )
                    Text(
                        formatNumberWithCommas(repaymentAmount),
                        fontWeight= FontWeight.Light
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),

                ){
                    Text(
                        "Transaction Id: ",
                        fontWeight= FontWeight.Bold
                    )
                    Text(
                        transactionId,
                        fontWeight= FontWeight.Light
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),

                ){
                    Text(
                        "Loan Id: ",
                        fontWeight= FontWeight.Bold
                    )
                    Text(
                        loanId,
                        fontWeight= FontWeight.Light
                    )
                }
            }
        }
    }
}

@Composable
fun LoanItemCard(
    provider:String = "M-shwari",
    loanAmount:Long = 10000L,
    amountPaid:Long = 2000L,
    loanBalance:Long = 0L,
    onClickPayment:() -> Unit

){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Warehouse,
                    contentDescription = "loan bank icon"
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(4f),
                verticalArrangement = Arrangement.Center
            ) {

                KeyValueText("Provider: ", provider)
                KeyValueText("Loan Amount: ", formatNumberWithCommas(loanAmount))
                KeyValueText("Amount Paid: ", formatNumberWithCommas(amountPaid))
                KeyValueText("Loan Balance: ", formatNumberWithCommas(loanBalance))

            }


        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onClickPayment) {

                    Text(text = "Pay ")
                }
            }
        }
    }


    }

@Composable
private fun KeyValueText(key:String = "key",value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            key ,
            fontWeight = FontWeight.Bold,
            softWrap = true
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun LoanItemPreview(){
    DoctorDeniTheme {
        LoanItemCard(
            onClickPayment = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PaymentCardPreview(){
    DoctorDeniTheme {
        PaymentCard()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PaymentsPreview() {
    DoctorDeniTheme {
        Payments()
    }
}

