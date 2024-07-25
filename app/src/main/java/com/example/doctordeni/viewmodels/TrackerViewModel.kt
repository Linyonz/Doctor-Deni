package com.example.doctordeni.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.doctordeni.models.requests.AggregateStats
import com.example.doctordeni.models.requests.LoanData
import com.example.doctordeni.models.requests.PaymentData
import com.example.doctordeni.models.responses.LoansProvidersItem
import com.example.doctordeni.models.states.LoanStatusEnum
import com.example.doctordeni.utils.MiscellaniousCode.daysUntilLoan
import com.example.doctordeni.utils.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.forEach
import okhttp3.internal.filterList

class TrackerViewModel(
    private val db: DatabaseReference, private val sessionService: SessionManager
) : ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val _loans = MutableStateFlow<List<LoanData>>(emptyList())
    val loans: StateFlow<List<LoanData>> = _loans.asStateFlow()

    private val _loansClosed = MutableStateFlow<List<LoanData>>(emptyList())
    val loansClosed: StateFlow<List<LoanData>> = _loansClosed.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentData>>(emptyList())
    val payments: StateFlow<List<PaymentData>> = _payments.asStateFlow()

    private val _dbError = MutableStateFlow<DatabaseError?>(null)
    val dbError: StateFlow<DatabaseError?> = _dbError.asStateFlow()


    private val _stats = MutableStateFlow<AggregateStats>(AggregateStats())
    val stats: StateFlow<AggregateStats> = _stats.asStateFlow()

    private val _loanProviders = MutableStateFlow<List<LoansProvidersItem>?>(null)
    val loanProviders:StateFlow<List<LoansProvidersItem>?> = _loanProviders.asStateFlow()


    init {
        getLoans()
        getPayments()
        getLoanProviders()
        _stats.value.userName = sessionService.getUserEmail()?.split("@")?.first() ?: "user"
    }

    fun selectLoan(loan: LoanData) {
        sessionService.saveSelectedLoan(loan)
    }

    fun getSelectedLoan(): LoanData? {
        return sessionService.fetchSelectedLoan()
    }

    fun clearLoanInfo(){
        sessionService.removeSelectedLoan()
        sessionService.removeSelectedLoanStrategy()
    }

    fun addLoanData(loan: LoanData) {
        loan.userEmail = sessionService.getUserEmail()
        getMainKeyRef().child("loans").child(loan.loanId!!).setValue(loan)
    }

    fun updateLoanItem( repaymentAmount:Long){
        val loanData = sessionService.fetchSelectedLoan()!!
        val totalRepayment = loanData.amountPaid!!.plus(repaymentAmount)
        if(totalRepayment >= loanData.loanAmount!!){
            loanData.loanStatus = LoanStatusEnum.CLOSED
        }
        loanData.amountPaid = totalRepayment
        addLoanData(loanData)
    }

    fun getLoanItem(loanId: String){
        Log.d("getLoanItem","loan selected: ${loanId}")
        val loan = getMainKeyRef().child("loans").child(loanId)
        loan.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val loanData = snapshot.getValue(LoanData::class.java)
                Log.e("SIngle loan", loanData?.toString()!!)
//                _selectedLoan.value = loanData
            }

            override fun onCancelled(error: DatabaseError) {
                _dbError.value = error
            }
        })
    }

    fun addPaymentData(payment: PaymentData) {
        payment.loanId = sessionService.fetchSelectedLoan()?.loanId
        payment.userEmail = sessionService.getUserEmail()
        Log.e("addPaymentData", payment.toString())
//        getLoanItem(_selectedLoan.value?.loanId!!)
        updateLoanItem(payment.repaymentAmount!!)
        getMainKeyRef().child("payments").child(payment.repaymentId!!).setValue(payment)
    }


    private fun getMainKeyRef(): DatabaseReference {
        return db.child(sessionService.getUserEmail()?.split("@")?.first() ?: "user")
    }

    fun getLoans() {
        val loans =  getMainKeyRef().child("loans")
        loans.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull { it.getValue(LoanData::class.java) }
                _loans.value = dataList.filterNot { it.loanStatus == LoanStatusEnum.CLOSED }
                _loansClosed.value = dataList.filter { it.loanStatus == LoanStatusEnum.CLOSED }
                getStats()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TrackerViewModelLoans", "Error getting loans: ${error.message}")
                _dbError.value = error

            }
        })
    }

    fun getPayments(){
        val payments = getMainKeyRef().child("payments")
        payments.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paymentList = snapshot.children.mapNotNull { it.getValue(PaymentData::class.java) }
                val loansNotClosed = _loans.value.filterNot { it.loanStatus == LoanStatusEnum.CLOSED }
                val loanIdsNotClosed = loansNotClosed.map { it.loanId }
                val paymentsNotClosed = paymentList.filter { it.loanId in loanIdsNotClosed }
                _payments.value = paymentsNotClosed
                getStats()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TrackerViewModelPayments", "Error getting payments: ${error.message}")
                _dbError.value = error
            }

        })
    }

    private fun getStats(){
        if (_loans.value.isNotEmpty()) {
            _stats.value.loanCount = _loans.value.size
            val loansNotClosed = _loans.value.filterNot { it.loanStatus == LoanStatusEnum.CLOSED }
            var sum: Long = 0
            var totalDays:Long = 0
            loansNotClosed.forEach {
                sum += it.loanAmount!!
                if(it.strategy != null){
                    totalDays+=it.strategy?.daysToDueDate!!
                }else {
                    totalDays += daysUntilLoan(it.loanDueDate!!)?.toInt()!!
                }
            }
            _stats.value.loanTotal = sum
            _stats.value.daysUntilFinalPayment = totalDays
        }

        if(_loansClosed.value.isNotEmpty()){
            _stats.value.loanCountClosed = loansClosed.value.size
            var sum:Long = 0
            _loansClosed.value.forEach {
                sum += it.loanAmount!!
            }
            _stats.value.loanTotalClosed = sum
        }

        if (_payments.value.isNotEmpty()) {
            val loansNotClosed = _loans.value.filterNot { it.loanStatus == LoanStatusEnum.CLOSED }
            val loanIdsNotClosed = loansNotClosed.map { it.loanId }
            val paymentsNotClosed = _payments.value.filter { it.loanId in loanIdsNotClosed }
            _stats.value.paymentCount = paymentsNotClosed.size
            var sum: Long = 0
            paymentsNotClosed.forEach {
                sum += it.repaymentAmount!!
            }
            _stats.value.paymentTotal = sum
        }

    }

    private fun getLoanProviders(){
        val providers = db.child("loaners")
        providers.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val datalist = snapshot.children.mapNotNull { it.getValue(LoansProvidersItem::class.java) }
                _loanProviders.value = datalist
            }

            override fun onCancelled(error: DatabaseError) {
                _dbError.value = error
            }

        })
    }

    fun getPaymentsByLoanId(): Map<String?, List<PaymentData>> {
       return  payments.value.groupBy { it.loanId }
    }

}