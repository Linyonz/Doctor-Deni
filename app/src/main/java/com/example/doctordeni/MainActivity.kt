package com.example.doctordeni

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.navigation.TabItem
import com.example.doctordeni.services.AccountService
import com.example.doctordeni.ui.screens.Content
import com.example.doctordeni.ui.screens.Home
import com.example.doctordeni.ui.screens.ProfileScreen
import com.example.doctordeni.ui.screens.Strategy
import com.example.doctordeni.ui.screens.Track
import com.example.doctordeni.ui.screens.content.ContentDetail
import com.example.doctordeni.ui.screens.strategy.LoanCalculator
import com.example.doctordeni.ui.screens.strategy.StrategyDetailScreen
import com.example.doctordeni.ui.screens.tracking.BanksScreen
import com.example.doctordeni.ui.screens.tracking.CustomProviderScreen
import com.example.doctordeni.ui.screens.tracking.LendingAppsScreen
import com.example.doctordeni.ui.screens.tracking.LoanDetail
import com.example.doctordeni.ui.screens.tracking.PaymentDetail
import com.example.doctordeni.ui.screens.tracking.ProviderLoanScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.SessionManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val accountService: AccountService by inject()
        val sessionManager: SessionManager by inject()
        val bottomItems = listOf(
            BottomNavigation.Home,
            BottomNavigation.Track,
            BottomNavigation.Strategy,
            BottomNavigation.Content,
        )

        setContent {
            DoctorDeniTheme(
                darkTheme = sessionManager.getAppTheme()
            ) {

                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                val context = LocalContext.current
                val isDarkMode = sessionManager.getAppTheme()

                ModalNavigationDrawer(
                    drawerState = drawerState, drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerItem(icon = {
                                Icon(
                                    imageVector = Icons.Filled.LightMode,
                                    contentDescription = "Light Mode"
                                )
                            },
                                label = { if (isDarkMode) Text("LightMode") else Text("DarkMode") },
                                selected = false,
                                onClick = {
                                    sessionManager.saveAppTheme(!isDarkMode)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                                    } else {
                                        recreate()
                                    }
                                })

                            NavigationDrawerItem(icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Logout"
                                )
                            }, label = { Text("Logout") }, selected = false, onClick = {

                                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                                startActivity(intent)

                                scope.launch {
                                    sessionManager.clear()
                                    accountService.signOut()
                                    drawerState.close()
                                }
                            })

                        }
                    }, gesturesEnabled = true
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        NavigationBar(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ) {
                            bottomItems.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    label = { Text(text = screen.route, textAlign = TextAlign.Center) },
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                inclusive = false
//                                        saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            screen.icon, contentDescription = screen.route
                                        )
                                    })
                            }
                        }

                    }) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = BottomNavigation.Home.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(BottomNavigation.Home.route) {
                                Home(navController)
                            }
                            composable(BottomNavigation.Strategy.route) {
                                Strategy(navController)
                            }
                            composable(BottomNavigation.Track.route) {
                                Track(navController)
                            }
                            composable(BottomNavigation.Content.route) {
                                Content(navController)
                            }
                            composable(BaseNav.Profile.route){
                                ProfileScreen()
                            }
                            composable(BaseNav.ContentDetail.route) {
                                ContentDetail()
                            }
                            composable(BaseNav.LoanDetail.route) {
                                LoanDetail(navController)
                            }
                            composable(BaseNav.StrategyDetail.route) {
                                StrategyDetailScreen(navController)
                            }
                            composable(BaseNav.LoanProvider.route){
                                ProviderLoanScreen(navController)
                            }
                            composable(BaseNav.PaymentDetail.route){
                                PaymentDetail(navController)
                            }
                            composable(BaseNav.LoanCalculator.route){
                                LoanCalculator(navController)
                            }
                            composable(TabItem.Banks.route){
                                BanksScreen(navController)
                            }
                            composable(TabItem.LendingApp.route){
                                LendingAppsScreen(navController)
                            }
                            composable(TabItem.CustomProvider.route){
                                CustomProviderScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoctorDeniTheme {
    }
}