package com.example.doctordeni.ui.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doctordeni.R
import com.example.doctordeni.models.states.ScreenNav
import com.example.doctordeni.navigation.TabItem
import com.example.doctordeni.ui.screens.tracking.Loans
import com.example.doctordeni.ui.screens.tracking.Payments
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Track(nabHostController: NavHostController? = null) {
    val tabs = listOf(TabItem.Loans, TabItem.Payments)
    val pagerState = rememberPagerState(pageCount = {
        tabs.size
    })

    Column(modifier = Modifier.padding(18.dp)) {
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(navHostController = nabHostController, pagerState = pagerState)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun TrackPreview() {
    DoctorDeniTheme {
        Track()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }, colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.tertiary,
            navigationIconContentColor = MaterialTheme.colorScheme.tertiary,
            actionIconContentColor = MaterialTheme.colorScheme.tertiary,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    DoctorDeniTheme {
        TopBar()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState, scrollable:Boolean = false) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    when(scrollable){
        true -> {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.tertiary,
                edgePadding = 1.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    // OR Tab()
                    LeadingIconTab(
                        modifier = Modifier.padding(8.dp),
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
        false -> {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.tertiary
            ) {
                tabs.forEachIndexed { index, tab ->
                    // OR Tab()
                    LeadingIconTab(
                        modifier = Modifier.padding(8.dp),
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
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    DoctorDeniTheme {
        val tabs = listOf(
            TabItem.Loans, TabItem.Payments
        )
        val pagerState = rememberPagerState(pageCount = {
            tabs.size
        })
        Tabs(tabs = tabs, pagerState = pagerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(navHostController: NavHostController? = null, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> Loans(navHostController)
            1 -> Payments(navHostController)
        }
    }
}

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContentPreview() {
    DoctorDeniTheme {

        val tabs = listOf(
            TabItem.Loans, TabItem.Payments
        )
        val pagerState = rememberPagerState(pageCount = {
            tabs.size
        })
        TabsContent(navHostController = null, pagerState = pagerState)
    }
}