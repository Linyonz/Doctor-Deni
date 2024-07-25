package com.example.doctordeni.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.doctordeni.ui.screens.strategy.CalculatorScreen
import com.example.doctordeni.ui.screens.strategy.LoanStrategyScreen

sealed class StrategyTabs(var icon: ImageVector, var title: String, var screen: ComposableFun) {
    object Strategy : StrategyTabs(Icons.Filled.LockClock, "Loan Strategy", { LoanStrategyScreen() })
    object Calculator : StrategyTabs(Icons.Filled.Calculate, "Calculator", { CalculatorScreen() })
}