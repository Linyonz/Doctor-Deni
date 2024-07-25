package com.example.doctordeni.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavigation(val route: String, val icon:ImageVector) {
    object Home: BottomNavigation("Home", Icons.Filled.Home)
    object Strategy: BottomNavigation("Repayment Plan",Icons.Filled.Lightbulb)
    object Track: BottomNavigation("Track", Icons.Filled.AddCircle)
    object Content: BottomNavigation("Content",Icons.Filled.Newspaper)
}
