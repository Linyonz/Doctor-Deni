package com.example.doctordeni.ui.screens.strategy

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.states.ScreenNav
import com.example.doctordeni.models.states.ScreenStateEnum
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.ui.components.StateScreen
import com.example.doctordeni.ui.screens.tracking.LoanCard
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoanStrategyScreen(
    navHostController: NavHostController? = null
) {
    val trackerViewModel = koinViewModel<TrackerViewModel>()



    val loanList = trackerViewModel.loans.collectAsState()
    Scaffold(

    ) { paddingValues ->

        if (loanList.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(loanList.value){
                    var expanded by remember{ mutableStateOf(false) }
                    LoanCard(
                        onPlanButtonClick = {
                            trackerViewModel.selectLoan(it)
                            navHostController?.navigate(BaseNav.StrategyDetail.route)
                        },
                        onPayButtonClick = {
                            trackerViewModel.selectLoan(it)
                            navHostController?.navigate(BaseNav.PaymentDetail.route)
                        },
                        loanAmount = it.loanAmount,
                        amountPaid = it.amountPaid,
                        loanDate = it.loanDate!!,
                        loanDueDate = it.loanDueDate!!,
                        interestRate = it.interestRate!!,
                        loanStatusEnum = it.loanStatus!!,
                        serviceProvider = it.serviceProvider!!,
                        strategy = it.strategy,
                        expanded = expanded
                    ){
                        expanded = !expanded
                    }
                }
            }
        }

        if (loanList.value.isEmpty()) {
            StateScreen(screenStateEnum = ScreenStateEnum.EMPTY)
        }
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun LoanStratPreview(){
    DoctorDeniTheme {
        LoanStrategyScreen()
    }
}