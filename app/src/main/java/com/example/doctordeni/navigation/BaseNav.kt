package com.example.doctordeni.navigation

sealed class BaseNav(val route:String) {
    object ContentDetail:BaseNav("content-detail")
    object LoanDetail:BaseNav("loan-detail")
    object StrategyDetail:BaseNav("strategy-detail")
    object PaymentDetail:BaseNav("payment-detail")
    object Profile:BaseNav("profile")
    object LoanCalculator:BaseNav("loan-calculator")
    object LoanProvider:BaseNav("loan-provider")

}