package com.example.doctordeni.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.doctordeni.R

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: ImageVector, var title: String, var route: String) {
    object Payments : TabItem(Icons.Filled.Payments, "Payments" ,"Payments")
    object Loans : TabItem(Icons.Filled.Money, "Loans", "Loans")
    object Banks:TabItem(Icons.Filled.AccountBalance,"Banks","Banks")
    object LendingApp:TabItem(Icons.Filled.AppShortcut,"Lending Apps","LendingApps")
    object CustomProvider:TabItem(Icons.Filled.EditNote,"Custom Provider","CustomProvider")
}