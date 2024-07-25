package com.example.doctordeni.ui.screens.tracking

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.models.requests.StrategyData
import com.example.doctordeni.models.states.FrequencyStateEnum
import com.example.doctordeni.models.states.LoanStatusEnum
import com.example.doctordeni.models.states.ScreenNav
import com.example.doctordeni.models.states.ScreenStateEnum
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.ui.components.StateScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.EXPANSTION_TRANSITION_DURATION
import com.example.doctordeni.utils.MiscellaniousCode.formatNumberWithCommas
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Loans(navigationHostController: NavHostController? = null) {

    val popupActive = remember { mutableStateOf(false) }
    val popupPaymentActive = remember { mutableStateOf(false) }
    val trackerViewModel = koinViewModel<TrackerViewModel>()


    val loanList = trackerViewModel.loans.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = { popupActive.value = true }, modifier = Modifier.padding(32.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Loan")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        when {
            popupActive.value -> {
                navigationHostController?.navigate(BaseNav.LoanProvider.route)
            }

            popupPaymentActive.value -> {
                navigationHostController?.navigate(BaseNav.PaymentDetail.route)

            }
        }

        if (loanList.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(loanList.value) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            var expanded by remember{ mutableStateOf(false) }
                            LoanCard(
                                onPayButtonClick = {
                                    trackerViewModel.selectLoan(it)
                                    navigationHostController?.navigate(BaseNav.PaymentDetail.route)
                                },
                                onPlanButtonClick = {
                                    trackerViewModel.selectLoan(it)
                                    navigationHostController?.navigate(BaseNav.StrategyDetail.route)
                                },
                                loanAmount = it.loanAmount,
                                amountPaid = it.amountPaid,
                                loanDate = it.loanDate!!,
                                loanDueDate = it.loanDueDate!!,
                                interestRate = it.interestRate!!,
                                loanStatusEnum = it.loanStatus!!,
                                serviceProvider = it.serviceProvider!!,
                                strategy =  it.strategy,
                                expanded = expanded
                            ){
                                expanded = !expanded
                            }

                        }
                    }
                }

            }
        }

        if (loanList.value.isEmpty()) {
            StateScreen(screenStateEnum = ScreenStateEnum.EMPTY)
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun LoansPreview() {
    DoctorDeniTheme {
        Loans()
    }
}


@Composable
fun LoanCard(
    onPayButtonClick: () -> Unit,
    onPlanButtonClick: () -> Unit,
    loanAmount:Long? = 12345,
    amountPaid:Long? = 1234,
    loanDate:String ="2024-04-30",
    loanDueDate:String ="2024-04-30",
    interestRate:Float = 12.5F,
    loanStatusEnum: LoanStatusEnum = LoanStatusEnum.OPEN,
    serviceProvider:String ="M-Shwari",
    expanded:Boolean = false,
    strategy:StrategyData? = null,
    onCardArrowClick: () -> Unit
){
    OutlinedCard(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        enabled = loanStatusEnum == LoanStatusEnum.OPEN,

    ) {

        val transitionState = remember {
            MutableTransitionState(expanded).apply {
                targetState = !expanded
            }
        }
        val transition = updateTransition(transitionState, label = "transition")
        val arrowRotationDegree by transition.animateFloat({
            tween(durationMillis = EXPANSTION_TRANSITION_DURATION)
        }, label = "rotationDegreeTransition") {
            if (expanded) 0f else 180f
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier= Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Service Provider: $serviceProvider",
                    fontSize = 18.sp
                )
                Text(
                    text = "Loan Amount: ${formatNumberWithCommas(loanAmount!!)}",
                    fontSize = 18.sp
                )
                Text(
                    text = "Amount Paid: ${formatNumberWithCommas(amountPaid!!)}",
                    fontSize = 18.sp
                )
                Text(
                    text = "Loan Balance: ${formatNumberWithCommas(loanAmount?.minus(amountPaid!!)!!)}",
                    fontSize = 18.sp
                )
                Text(
                    text = "Loan  Date: $loanDate",
                    fontSize = 18.sp
                )
                Text(
                    text = "Loan Due Date: $loanDueDate",
                    fontSize = 18.sp
                )
                Text(
                    text = "Interest Rate: $interestRate",
                    fontSize = 18.sp
                )

            }
        }

        if(strategy!= null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Box {
                        CardArrow(
                            degrees = arrowRotationDegree,
                            onClick = onCardArrowClick
                        )
                        CardTitle(title = "Plan")
                    }
                    ExpandableContent(visible = expanded, strategy = strategy)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier= Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onPayButtonClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(text = "Pay")
                }
            }

            Column(
                modifier= Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onPlanButtonClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(text = "Plan")
                }
            }
        }


    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
fun ExpandableContent(
    visible: Boolean = true,
    strategy: StrategyData? = null
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(
                text = "Time to clear loan: ${strategy?.message}",
                fontSize = 18.sp
            )
            Text(
                text = "Installment Amount: ${formatNumberWithCommas(strategy?.loanInstallmentAmount ?: 0L)}",
                fontSize = 18.sp
            )
            Text(
                text = "Installment frequency: ${strategy?.frequencyStateEnum?.name?.capitalize(
                    Locale.current)}",
                fontSize = 18.sp
            )
            Text(
                text = "Installment count: ${formatNumberWithCommas(strategy?.loanInstallmentCount?: 0L)}",
                fontSize = 18.sp
            )
        }
    }
}
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, device = Devices.PIXEL_4_XL)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun LoanCardPreview(){
    DoctorDeniTheme {
        LoanCard({},{}){

        }
    }
}
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, device = Devices.PIXEL_4_XL)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun ExpandedCardPreview(){
    DoctorDeniTheme {
        ExpandableContent(
            true,
            strategy = StrategyData(
            message ="2 years to go",
                loanBalance = 1234567,
                frequencyStateEnum = FrequencyStateEnum.WEEKLY,
                loanAmount = 123456789,
                loanInstallmentAmount = 12345,
                loanInstallmentCount = 1234567.div(12345))
        )
    }
}