package com.example.doctordeni.models.requests

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.doctordeni.models.states.LoanStatusEnum
import java.time.Clock
import java.time.Instant
import java.util.Date

data class LoanData(
    var loanId:String? = null,
    var loanDate:String? = null,
    var loanAmount:Long? = null,
    var loanDueDate:String? = null,
    var interestRate:Float? = 0F,
    var serviceProvider:String? = null,
    var userEmail:String? = null,
    var loanStatus:LoanStatusEnum? = LoanStatusEnum.OPEN
){
    var amountPaid:Long? = 0
    var strategy :StrategyData? = null

}
