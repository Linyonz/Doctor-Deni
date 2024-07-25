package com.example.doctordeni.ui.screens.strategy

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.R
import com.example.doctordeni.models.requests.StrategyData
import com.example.doctordeni.models.states.FrequencyStateEnum
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.LottieLoader
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode.formatNumberWithCommas
import com.example.doctordeni.viewmodels.StrategyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalculatorScreen(
    navHostController: NavHostController? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculate Loan Payment Strategies",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        LoanCalculator(navHostController)
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StrategyPreview() {
    DoctorDeniTheme {
        CalculatorScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanCalculator(navHostController: NavHostController? = null) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val strategyViewModel = koinViewModel<StrategyViewModel>()
            strategyViewModel.getSelectedStrat()
            val stratState = strategyViewModel.strategyData.collectAsState()


            val options = listOf("Daily", "Weekly", "Monthly", "Frequently")
            val enums = FrequencyStateEnum.entries.toTypedArray()
            var selectedEnum by remember { strategyViewModel.selectedEnum }
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { strategyViewModel.selectedOptionText }
//            var loanAmount by remember{ mutableIntStateOf(0) }

            var loanAmount by rememberSaveable { strategyViewModel.loanAmount }
            var installmentAmount by rememberSaveable { strategyViewModel.installmentAmount }

            var loanInformation = strategyViewModel.getSelectedLoan()

            // We want to react on tap/press on TextField to show menu
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                OutlinedTextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = {},
                    label = { Text("Payment Frequency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEachIndexed { index, selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                selectedEnum = enums[index]
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }


            OutlinedTextField(
                value = loanAmount,
                onValueChange = { loanAmount = it.trim() },
                label = { Text(text = "Enter the Loan Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = installmentAmount,
                onValueChange = { installmentAmount = it.trim() },
                label = { Text(text = "Enter the Loan installment amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Button(
                onClick = {
                    strategyViewModel.setStrategyState(
                        StrategyData(
                            loanAmount = if (loanAmount == "") null else loanAmount.toLong(),
                            loanInstallmentAmount = if (installmentAmount == "") null else installmentAmount.toLong(),
                            frequencyStateEnum = selectedEnum
                        )
                    )
                }, modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Calculate")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (stratState.value.loanAmount == null && stratState.value.loanInstallmentAmount == null) {
                        LottieLoader(
                            modifier = Modifier.weight(9f), resource = R.raw.empty1
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "No actionable input",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (stratState.value.loanAmount == null && stratState.value.loanInstallmentAmount != null) {
                        LottieLoader(
                            modifier = Modifier.weight(9f), resource = R.raw.empty1
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Loan Amount is empty",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (stratState.value.loanAmount != null && stratState.value.loanInstallmentAmount == null) {
                        LottieLoader(
                            modifier = Modifier.weight(8f), resource = R.raw.empty
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Loan Installment is empty",
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    if (stratState.value.minimumLoanInstallmentAmount != null) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 4.dp, end = 16.dp),
                            text = "Minimal Installment allowed: ${formatNumberWithCommas(stratState.value.minimumLoanInstallmentAmount!!)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    if (stratState.value.message != null) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 4.dp, end = 16.dp),
                            text = "Default time to clear loan: ${stratState.value.message}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    if (stratState.value.loanAmount != null && stratState.value.loanInstallmentAmount != null) {
//                    strategyViewModel.getLoanElapseTime(stratState.value)

                        if (installmentAmount != "" && installmentAmount.toLong() > 0L && installmentAmount <= loanAmount && loanAmount != "" && loanAmount.toLong() > 0L) {
                            val factor = loanAmount.toLong().div(installmentAmount.toLong())
                            Log.e("factor", "installment count: $factor")
                            val daysToEndDate = strategyViewModel.calculateMinimalRate(
                                factor, stratState.value.frequencyStateEnum
                            )
                            val loanTime = strategyViewModel.getPaymentString(
                                daysToEndDate, stratState.value.frequencyStateEnum
                            )
                            Log.e("time to clear", "loan time: $loanTime")

                            LottieLoader(
                                modifier = Modifier.weight(7f), resource = R.raw.success
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Your loan of ${formatNumberWithCommas(loanAmount.toLong())} with installments of ${
                                    formatNumberWithCommas(installmentAmount.toLong())
                                } paid ${stratState.value.frequencyStateEnum.name} will be paid off in $loanTime",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            if (stratState.value.minimumLoanInstallmentAmount != null) {
                                if (installmentAmount.toLong() >= stratState.value.minimumLoanInstallmentAmount!!.toLong()) {
                                    val stratD = stratState.value
                                    stratD.message = loanTime
                                    stratD.loanInstallmentAmount = installmentAmount.toLong()
                                    stratD.daysToDueDate = daysToEndDate
                                    stratD.loanInstallmentCount = factor
                                    loanInformation?.strategy = stratD
                                    Button(modifier = Modifier.weight(1f), onClick = {
                                        strategyViewModel.addLoanData(loanInformation!!)
                                        strategyViewModel.clearSelection()
                                        navHostController?.navigate(BottomNavigation.Strategy.route)
                                    }) {
                                        Text(text = "Apply")
                                    }
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
fun LoanCalculationPreview() {
    DoctorDeniTheme {
        LoanCalculator()
    }
}