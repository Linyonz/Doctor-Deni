package com.example.doctordeni.models.requests

import com.example.doctordeni.models.states.FrequencyStateEnum

data class StrategyData(
    var message:String? = null,
    var frequencyStateEnum: FrequencyStateEnum = FrequencyStateEnum.DAILY,
    var loanAmount:Long? = null,
    var loanBalance:Long? = null,
    var loanInstallmentAmount:Long? = null,
    var minimumLoanInstallmentAmount:Long? = null,
    var loanInstallmentCount:Long? = null,
    var daysToDueDate:Long? = null,
    )
