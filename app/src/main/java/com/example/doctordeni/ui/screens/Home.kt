package com.example.doctordeni.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.R
import com.example.doctordeni.models.requests.AggregateStats
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.LottieLoader
import com.example.doctordeni.ui.components.NoDataFoundCard
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode.convertDaysToYearsMonthsWeeksDays
import com.example.doctordeni.utils.MiscellaniousCode.formatNumberWithCommas
import com.example.doctordeni.viewmodels.TrackerViewModel
import com.jaikeerthick.composable_graphs.composables.pie.PieChart
import com.jaikeerthick.composable_graphs.composables.pie.model.PieData
import com.jaikeerthick.composable_graphs.composables.pie.style.PieChartColors
import com.jaikeerthick.composable_graphs.composables.pie.style.PieChartStyle
import com.jaikeerthick.composable_graphs.composables.pie.style.PieChartVisibility
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home(navController: NavHostController? = null) {
    val context = LocalContext.current
    val trackerViewModel = koinViewModel<TrackerViewModel>()


    val loanList = trackerViewModel.loans.collectAsState()
    val closedLoans = trackerViewModel.loansClosed.collectAsState()
    val paymentList = trackerViewModel.payments.collectAsState()
    val stats = trackerViewModel.stats.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            MainProfileBanner(navController, stats)
        }

        item {
            if (loanList.value.isNotEmpty()) {
                LoanElapsedCard(stats.value.daysUntilFinalPayment ?: 1000L)
            } else {
                LoanElapsedCard(1000)
            }
        }
        if (closedLoans.value.isNotEmpty()) {
            item {
                Text(
                    text = "Open Vs Closed Loan Count",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (closedLoans.value.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {


                        if (stats.value.loanTotalClosed!! > 0) {
                            val pieChartData = listOf(
                                PieData(
                                    value = stats.value.loanCountClosed?.toFloat()!!,
                                    label = "Cleared Loan Count",
                                    color = Color.Blue
                                ), PieData(
                                    value = stats.value.loanCount?.toFloat()!!,
                                    label = "Active Loan Count",
                                    color = Color.Green
                                )
                            )

                            // composable
                            PieChart(modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(top = 16.dp),
                                data = pieChartData,
                                style = PieChartStyle(
                                    visibility = PieChartVisibility(
                                        isLabelVisible = true, isPercentageVisible = true
                                    ), colors = PieChartColors(
                                        percentageColor = Color.White,
                                        labelColor = Color.White,
                                    )
                                ),
                                onSliceClick = { pieData ->
                                    Toast.makeText(context, "${pieData.label}", Toast.LENGTH_SHORT)
                                        .show()
                                })

                            StatsBox(
                                "Closed Loan Count",
                                "Open Loan Count",
                                stats.value.loanCountClosed?.toLong(),
                                stats.value.loanCount?.toLong()
                            )

                        }

                    }


                }
            }
        }
        if (closedLoans.value.isNotEmpty()) {
            item {
                Text(
                    text = "Open Vs Closed Loan Amounts",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (closedLoans.value.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {


                        if (stats.value.loanTotalClosed!! > 0) {
                            val pieChartData = listOf(
                                PieData(
                                    value = stats.value.loanTotalClosed?.toFloat()!!,
                                    label = "Cleared Loan Amount",
                                    color = Color.Blue
                                ), PieData(
                                    value = stats.value.loanTotal?.toFloat()!!,
                                    label = "Active Loan Amount",
                                    color = Color.Green
                                )
                            )

                            // composable
                            PieChart(modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 16.dp),
                                data = pieChartData,
                                style = PieChartStyle(
                                    visibility = PieChartVisibility(
                                        isLabelVisible = true, isPercentageVisible = true
                                    ), colors = PieChartColors(
                                        percentageColor = Color.White,
                                        labelColor = Color.White,
                                    )
                                ),
                                onSliceClick = { pieData ->
                                    Toast.makeText(context, "${pieData.label}", Toast.LENGTH_SHORT)
                                        .show()
                                })

                            StatsBox(
                                "Closed Loan Amount",
                                "Open Loan Amount",
                                stats.value.loanTotalClosed,
                                stats.value.loanTotal
                            )

                        }

                    }


                }
            }
        }

        item {
            Text(
                text = "Repayment Progress",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)

            )
        }
        item {
            if (loanList.value.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {


                        if (stats.value.loanTotal!! > 0) {
                            val pieChartData = listOf(
                                PieData(
                                    value = stats.value.loanTotal?.toFloat()!!,
                                    label = "Loaned Amount",
                                    color = Color.Blue
                                ), PieData(
                                    value = stats.value.paymentTotal?.toFloat()!!,
                                    label = "Repaid Amount",
                                    color = Color.Green
                                )
                            )

                            // composable
                            PieChart(modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 16.dp),
                                data = pieChartData,
                                style = PieChartStyle(
                                    visibility = PieChartVisibility(
                                        isLabelVisible = true, isPercentageVisible = true
                                    ), colors = PieChartColors(
                                        percentageColor = Color.White,
                                        labelColor = Color.White,
                                    )
                                ),
                                onSliceClick = { pieData ->
                                    Toast.makeText(context, "${pieData.label}", Toast.LENGTH_SHORT)
                                        .show()
                                })
                            StatsBox(
                                "Loaned Amount",
                                "Repaid Amount",
                                stats.value.loanTotal,
                                stats.value.paymentTotal
                            )


                        }

                    }


                }
            } else {
                NoDataFoundCard()
            }
        }

        item {
            Text(
                text = "Loan Balance Analysis",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (paymentList.value.isNotEmpty()) {
                        if (stats.value.paymentTotal!! > 0) {
                            val pieChartData = listOf(
                                PieData(
                                    value = stats.value.paymentTotal?.toFloat()!!,
                                    label = "Total Repaid Amount",
                                    color = Color.Green
                                ), PieData(
                                    value = stats.value.loanTotal?.minus(stats.value.paymentTotal!!)
                                        ?.toFloat()!!, label = "Loan Balance", color = Color.Blue
                                )
                            )

                            // composable
                            PieChart(modifier = Modifier.padding(vertical = 16.dp),
                                data = pieChartData,
                                style = PieChartStyle(
                                    visibility = PieChartVisibility(
                                        isLabelVisible = true, isPercentageVisible = true
                                    ), colors = PieChartColors(
                                        percentageColor = Color.White,
                                        labelColor = Color.White,
                                    )
                                ),
                                onSliceClick = { pieData ->
                                    Toast.makeText(context, "${pieData.label}", Toast.LENGTH_SHORT)
                                        .show()
                                })

//                            Text(
//                                text = "${
//                                    (stats.value.paymentTotal!!.div(stats.value.loanTotal!!))
//                                        .times(100)
//                                }% paid",
//                                style = MaterialTheme.typography.headlineLarge,
//                                modifier = Modifier.padding( 16.dp),
//                                fontWeight = FontWeight.ExtraBold,
//                                fontSize = 30.sp
//                            )
                        }

                    } else {
                        NoDataFoundCard()
                    }
                }
                if (paymentList.value.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        StatsBox(
                            "Total Paid",
                            "Balance Due",
                            stats.value.paymentTotal,
                            stats.value.loanTotal?.minus(stats.value.paymentTotal!!)
                        )
                    }
                }
            }


        }
        item {
            ContentCard(navController)
        }

    }
}

@Composable
private fun StatsBox(
    firstString: String = "firstString",
    secondString: String = "secondString",
    firstValue: Long? = 0L,
    secondValue: Long? = 0L
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            InformatioCard(
                firstString, firstValue
            )
        }
        item {
            InformatioCard(
                secondString, secondValue
            )
        }

    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StatsBoxPreview() {
    DoctorDeniTheme {
        StatsBox()
    }
}

@Composable
private fun ContentCard(navController: NavHostController? = null) {
    val gradient = Brush.linearGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary),
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(6.dp)
            .border(
                BorderStroke(4.dp, gradient), shape = RoundedCornerShape(8.dp)
            )
            .background(gradient)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = true, onClick = {
                    navController?.navigate(BottomNavigation.Content.route)
                })
                .border(
                    BorderStroke(4.dp, gradient), shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Go to Content",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 36.sp
            )
        }
    }
}

@Composable
private fun LoanElapsedCard(
    days: Long = 1000
) {
    OutlinedCard(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardColors(
            containerColor = Color.Blue,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "You are almost Debt Free!",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 30.sp,
                lineHeight = 24.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "${convertDaysToYearsMonthsWeeksDays(days.toLong())} to go",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.weight(3f),
                    minLines = 2,
                    maxLines = 4
                )
                LottieLoader(
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp), resource = R.raw.snoop_dance
                )

            }
        }
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun LoanElapsedPreview() {
    DoctorDeniTheme {
        LoanElapsedCard()
    }
}

@Composable
private fun MainProfileBanner(
    navController: NavHostController? = null, stats: State<AggregateStats>? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp)
            .clickable(enabled = true, onClick = {
                navController?.navigate(BaseNav.Profile.route)
            })
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Filled.Person,
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.secondary, shape = CircleShape),
                contentDescription = "Profile"
            )
        }
        Column(
            modifier = Modifier
                .weight(8f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hi ${stats?.value?.userName}",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Track and celebrate your debt journey",
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PreviewMainProfileBanner() {
    DoctorDeniTheme {
        MainProfileBanner(navController = null, stats = null)
    }
}


@Composable
fun InformatioCard(
    title: String = "Title", number: Long? = 2
) {
    OutlinedCard(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = formatNumberWithCommas(number!!),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun InformationCardPreview() {
    DoctorDeniTheme {
        InformatioCard()
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun HomePreview() {
    DoctorDeniTheme {
        Home()
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun ContentCardPreview() {
    DoctorDeniTheme {
        ContentCard()
    }
}