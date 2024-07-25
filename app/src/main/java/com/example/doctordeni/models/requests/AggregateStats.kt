package com.example.doctordeni.models.requests

data class AggregateStats(
    var loanCount:Int? = 0,
    var loanCountClosed:Int? = 0,
    var loanTotal:Long? = 0,
    var loanTotalClosed:Long? = 0,
    var paymentCount:Int? = 0,
    var paymentCountClosed:Int? = 0,
    var paymentTotal:Long? = 0,
    var paymentTotalClosed:Long? = 0,
    var userName:String? = "user",
    var daysUntilFinalPayment:Long? = 0
)
