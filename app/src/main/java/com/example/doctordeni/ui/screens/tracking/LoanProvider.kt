package com.example.doctordeni.ui.screens.tracking

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doctordeni.navigation.TabItem
import com.example.doctordeni.ui.screens.Tabs
import com.example.doctordeni.ui.theme.DoctorDeniTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProviderLoanScreen(navHostController: NavHostController? = null) {
    val tabs = listOf(TabItem.Banks, TabItem.LendingApp, TabItem.CustomProvider)
    val pagerState = rememberPagerState(pageCount = {
        tabs.size
    })

    Column(modifier = Modifier.padding(18.dp)) {
        Tabs(tabs = tabs, pagerState = pagerState, scrollable = true)
        ProviderLoanContent(navHostController = navHostController, pagerState = pagerState)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun ProviderLoanPreview() {
    DoctorDeniTheme {
        ProviderLoanScreen()
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProviderLoanContent(navHostController: NavHostController? = null, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> BanksScreen(navHostController)
            1 -> LendingAppsScreen(navHostController)
            2 -> CustomProviderScreen(navHostController)
        }
    }
}

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProviderLoanContentPreview() {
    DoctorDeniTheme {

        val tabs = listOf(TabItem.Banks, TabItem.LendingApp, TabItem.CustomProvider)
        val pagerState = rememberPagerState(pageCount = {
            tabs.size
        })
        ProviderLoanContent(navHostController = null, pagerState = pagerState)
    }
}