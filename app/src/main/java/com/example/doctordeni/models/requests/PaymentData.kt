package com.example.doctordeni.models.requests

import com.example.doctordeni.models.states.LoanStatusEnum
import java.util.UUID

data class PaymentData(
    var repaymentId: String? = UUID.randomUUID().toString(),
    var repaymentDate: String? = null,
    var repaymentAmount: Long? = null,
    var transactionId: String? = null,
    var userEmail: String? = null,
    var loanId: String? = null
) {
    var loanStatus: LoanStatusEnum? = LoanStatusEnum.OPEN
}
