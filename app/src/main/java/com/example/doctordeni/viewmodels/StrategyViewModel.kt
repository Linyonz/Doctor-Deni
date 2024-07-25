package com.example.doctordeni.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.doctordeni.models.requests.LoanData
import com.example.doctordeni.models.requests.StrategyData
import com.example.doctordeni.models.states.FrequencyStateEnum
import com.example.doctordeni.utils.MiscellaniousCode.convertDaysToYearsMonthsWeeksDays
import com.example.doctordeni.utils.SessionManager
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StrategyViewModel(
    private val db: DatabaseReference, private val sessionManager: SessionManager
) : ViewModel() {

    private val _strategyData = MutableStateFlow<StrategyData>(StrategyData())
    val strategyData: StateFlow<StrategyData> = _strategyData.asStateFlow()

    private val _selectedStrat = MutableStateFlow<StrategyData>(StrategyData())
    val selectedStrat: StateFlow<StrategyData> = _selectedStrat.asStateFlow()

    val options = listOf("Daily", "Weekly", "Monthly", "Frequently")
    val enumz = FrequencyStateEnum.entries.toTypedArray()

    val enums =
        FrequencyStateEnum.entries.toTypedArray().filterNot { it == FrequencyStateEnum.FREQUENTLY }

    var loanAmount = mutableStateOf("")
    var selectedEnum = mutableStateOf(FrequencyStateEnum.DAILY)
    var selectedOptionText = mutableStateOf(options[0])
    var installmentAmount = mutableStateOf("")

    fun setStrategyState(strategyData: StrategyData) {
        Log.e("strategyState", "setStrategyState: $strategyData")
        _strategyData.value = strategyData
    }


    private fun getMainKeyRef(): DatabaseReference {
        return db.child(sessionManager.getUserEmail()?.split("@")?.first() ?: "user")
    }

    fun selectLoan(loan: LoanData) {
        sessionManager.saveSelectedLoan(loan)
    }


    fun selectStrategy(strategyData: StrategyData) {
        sessionManager.saveSelectedLoanStrategy(strategyData)
    }

    fun getSelectedStrat() {

        if (sessionManager.fetchSelectedLoanStrategy() != null) {
            Log.e("selectedStrat", sessionManager.fetchSelectedLoanStrategy()?.toString()!!)
            val selectedStrat = sessionManager.fetchSelectedLoanStrategy()!!
            _strategyData.value = selectedStrat
            loanAmount.value = selectedStrat.loanBalance?.toString()!!
            val selectedState = selectedStrat.frequencyStateEnum
            options.forEachIndexed { index, s ->
                if (selectedState == enumz[index]) {
                    selectedOptionText.value = s
                    selectedEnum.value = enumz[index]
                }
            }

            setStrategyState(selectedStrat)
        }

    }

    fun getSelectedLoan(): LoanData? {
        return if (sessionManager.fetchSelectedLoan() != null) {
            Log.e(
                "getSelectedLoanStrat",
                sessionManager.fetchSelectedLoan()!!.strategy?.toString() ?: ""
            )
            sessionManager.fetchSelectedLoan()
        } else {
            null
        }
    }

    fun getLoanBalance(loanInformation: LoanData): Long {
        return loanInformation.loanAmount!! - loanInformation.amountPaid!!
    }

    fun addLoanData(loan: LoanData) {
        loan.userEmail = sessionManager.getUserEmail()
        getMainKeyRef().child("loans").child(loan.loanId!!).setValue(loan)
    }

    fun clearSelection() {
        sessionManager.removeSelectedLoanStrategy()
        _selectedStrat.value = StrategyData()
        sessionManager.removeSelectedLoan()
    }

    fun getLoanElapseTime(strategyData: StrategyData) {

        if (installmentAmount.value != "") {
            if (installmentAmount.value.toLong() > 0L) {
                strategyData.message = ""
                val factor = strategyData.loanBalance?.div(installmentAmount.value.toLong())!!
                Log.e("factor", "getLoanElapseTime: $factor")
                when (strategyData.frequencyStateEnum) {
                    FrequencyStateEnum.DAILY -> {
                        strategyData.message = convertDaysToYearsMonthsWeeksDays(totalDays = factor)
                        setStrategyState(strategyData)
                    }

                    FrequencyStateEnum.WEEKLY -> {
                        strategyData.message =
                            convertDaysToYearsMonthsWeeksDays(totalDays = factor * 7)
                        setStrategyState(strategyData)
                    }

                    FrequencyStateEnum.MONTHLY -> {
                        strategyData.message =
                            convertDaysToYearsMonthsWeeksDays(totalDays = factor * 30)
                        setStrategyState(strategyData)
                    }

                    FrequencyStateEnum.FREQUENTLY -> {
                        strategyData.message = " $factor times"
                        setStrategyState(strategyData)
                    }
                }
            }
        }
    }

    fun getPaymentString(daysUntilDueDate: Long, frequencyStateEnum: FrequencyStateEnum): String {
        return when (frequencyStateEnum) {
            FrequencyStateEnum.DAILY -> {
                convertDaysToYearsMonthsWeeksDays(totalDays = daysUntilDueDate)
            }

            FrequencyStateEnum.WEEKLY -> {
                convertDaysToYearsMonthsWeeksDays(totalDays = daysUntilDueDate)
            }

            FrequencyStateEnum.MONTHLY -> {
                convertDaysToYearsMonthsWeeksDays(totalDays = daysUntilDueDate)
            }

            FrequencyStateEnum.FREQUENTLY -> {
                convertDaysToYearsMonthsWeeksDays(totalDays = daysUntilDueDate)
            }
        }
    }

    fun getValidTimespan(days: Int): List<FrequencyStateEnum> {
        if (days < 7) {
            return enums.filter { it == FrequencyStateEnum.DAILY }
        }
        if (days >= 7 && days < 30) {
            return enums.filterNot { it == FrequencyStateEnum.MONTHLY }
        }
        return enums
    }

    fun calculateMinimalRate(dailyRate: Long, frequencyStateEnum: FrequencyStateEnum): Long {
        return when (frequencyStateEnum) {
            FrequencyStateEnum.DAILY -> {
                dailyRate.times(1)
            }

            FrequencyStateEnum.WEEKLY -> {
                dailyRate.times(7)
            }

            FrequencyStateEnum.MONTHLY -> {
                dailyRate.times(30)
            }

            FrequencyStateEnum.FREQUENTLY -> {
                dailyRate.times(1)
            }
        }
    }

}