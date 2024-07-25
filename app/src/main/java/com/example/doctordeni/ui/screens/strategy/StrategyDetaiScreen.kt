package com.example.doctordeni.ui.screens.strategy

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.requests.StrategyData
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.InformationCard
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode.daysUntilLoan
import com.example.doctordeni.utils.MiscellaniousCode.formatNumberWithCommas
import com.example.doctordeni.viewmodels.StrategyViewModel
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StrategyDetailScreen(
    navHostController: NavHostController? = null
) {
    val trackerViewModel = koinViewModel<TrackerViewModel>()
    val strategyViewModel = koinViewModel<StrategyViewModel>()
    val loanInformation = trackerViewModel.getSelectedLoan()

    val daysUntilDue = daysUntilLoan(loanInformation?.loanDueDate!!)
    val loanBalance = loanInformation.loanAmount!! - loanInformation.amountPaid!!
    val dailyRate = loanBalance.div(daysUntilDue!!)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Loan Information",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
            }

        item {

            OutlinedCard(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    InformationCard(
                        title = "Loan Id",
                        undertone = loanInformation.loanId,
                        imageVector = Icons.Filled.Key
                    )
                    InformationCard(
                        title = "Loan Date",
                        undertone = loanInformation.loanDate,
                        imageVector = Icons.Filled.CalendarToday
                    )
                    InformationCard(
                        title = "Loan Due Date",
                        undertone = loanInformation.loanDueDate,
                        imageVector = Icons.Filled.CalendarViewMonth
                    )
                    InformationCard(
                        title = "Loan Balance",
                        undertone = "Ksh. ${formatNumberWithCommas(loanBalance)}",
                        imageVector = Icons.Filled.Money
                    )
                    InformationCard(
                        title = "Amount Paid",
                        undertone = "Ksh. ${formatNumberWithCommas(loanInformation.amountPaid!!)}",
                        imageVector = Icons.Filled.AttachMoney
                    )
                    InformationCard(
                        title = "Days until loan Due",
                        undertone = daysUntilDue.toString(),
                        imageVector = Icons.Filled.CalendarViewDay
                    )
                }
            }
        }

        item {
            Text(
                text = "Select the most friendly configuration",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            val freq = strategyViewModel.getValidTimespan(daysUntilDue.toInt())

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(freq) {
                    val installmentLong = strategyViewModel.calculateMinimalRate(dailyRate, it)
                    val installmentString = formatNumberWithCommas(installmentLong)
                    val installmentCount = loanBalance.div(installmentLong)
                    val repaymentPeriod =
                        strategyViewModel.getPaymentString(daysUntilDue, it)
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .width(280.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            InformationCard(
                                title = "frequency",
                                undertone = it.name.capitalize(Locale.current),
                                imageVector = Icons.Filled.CalendarViewWeek
                            )
                            InformationCard(
                                title = "minimum Installment",
                                undertone = "Ksh. $installmentString",
                                imageVector = Icons.Filled.Money
                            )
                            InformationCard(
                                title = "Installment Count",
                                undertone = " $installmentCount installments",
                                imageVector = Icons.Filled.Repeat
                            )
                            InformationCard(
                                title = "Repayment Period",
                                undertone = " $repaymentPeriod",
                                imageVector = Icons.Filled.Payments
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                val strategyData = StrategyData(
                                    message = repaymentPeriod,
                                    frequencyStateEnum = it,
                                    loanAmount = loanInformation.loanAmount,
                                    loanBalance = loanBalance,
                                    loanInstallmentAmount = installmentLong,
                                    minimumLoanInstallmentAmount = installmentLong,
                                    loanInstallmentCount = installmentCount,
                                    daysToDueDate = daysUntilDue
                                )
                                Button(onClick = {
                                    loanInformation.strategy = strategyData
                                    strategyViewModel.selectLoan(loanInformation)
                                    strategyViewModel.selectStrategy(strategyData)
                                    //navigate to the loan calculator page
                                    navHostController?.navigate(BaseNav.LoanCalculator.route)
                                }) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
//                                    Text(text = "Edit")

                                }
                                Button(onClick = {

                                    loanInformation.strategy = strategyData
                                    strategyViewModel.addLoanData(loanInformation)
                                    navHostController?.navigate(BottomNavigation.Track.route)
                                }) {
                                    Icon(imageVector = Icons.Filled.Save, contentDescription = "Save")
                                }
                            }
                        }
                    }

                }
            }
        }
    }


}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StrategyDetailPreview() {
    DoctorDeniTheme {
        StrategyDetailScreen()
    }
}
