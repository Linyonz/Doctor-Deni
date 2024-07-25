package com.example.doctordeni.ui.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.navigation.StrategyTabs
import com.example.doctordeni.navigation.TabItem
import com.example.doctordeni.ui.screens.strategy.CalculatorScreen
import com.example.doctordeni.ui.screens.strategy.LoanCalculator
import com.example.doctordeni.ui.screens.strategy.LoanStrategyScreen
import com.example.doctordeni.ui.screens.tracking.Loans
import com.example.doctordeni.ui.screens.tracking.Payments
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Strategy(navController: NavHostController? = null) {

//    val tabs = listOf(StrategyTabs.Strategy, StrategyTabs.Calculator)
//    val pagerState = rememberPagerState(pageCount = {
//        tabs.size
//    })
    CalculatorScreen(navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StratPreview() {
    DoctorDeniTheme {
        Strategy()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StratTabs(tabs: List<StrategyTabs>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.tertiary
    ) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = { Icon(imageVector = tab.icon, contentDescription = "") },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun StratTabsPreview() {
    DoctorDeniTheme {
        val tabs = listOf(StrategyTabs.Strategy, StrategyTabs.Calculator)
        val pagerState = rememberPagerState(pageCount = {
            tabs.size
        })
        StratTabs(tabs = tabs, pagerState = pagerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StratTabsContent(navHostController: NavHostController ? = null, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> LoanStrategyScreen(navHostController)
            1 -> CalculatorScreen(navHostController)
        }
    }
}

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StratTabsContentPreview() {
    DoctorDeniTheme {

        val tabs = listOf(StrategyTabs.Strategy, StrategyTabs.Calculator)

        val pagerState = rememberPagerState(pageCount = {
            tabs.size
        })
        StratTabsContent(navHostController = null, pagerState = pagerState)
    }
}


