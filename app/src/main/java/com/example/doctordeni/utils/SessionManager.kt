package com.example.doctordeni.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.doctordeni.R
import com.example.doctordeni.models.requests.LoanData
import com.example.doctordeni.models.requests.StrategyData
import com.example.doctordeni.models.responses.Article
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson

class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_EMAIL = "user_email"
        const val SELECTED_ARTICLE = "selected-article"
        const val SELECTED_LOAN = "selected-loan"
        const val SELECTED_STRAT = "selected-strat"
        const val BOTTOM_CALC = "bottom-calc"
        const val APP_THEME = "app_theme"
    }

    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun saveArticle(user: Article){
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(SELECTED_ARTICLE,json)
        editor.apply()
    }

    fun saveSelectedLoan(loanData: LoanData){
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(loanData)
        editor.putString(SELECTED_LOAN,json)
        editor.apply()
    }

    fun removeSelectedLoan(){
        val editor = prefs.edit()
        editor.remove(SELECTED_LOAN)
        editor.apply()
    }
    fun saveSelectedLoanStrategy(strategyData: StrategyData){
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(strategyData)
        editor.putString(SELECTED_STRAT,json)
        editor.apply()
    }

    fun removeSelectedLoanStrategy(){
        val editor = prefs.edit()
        editor.remove(SELECTED_STRAT)
        editor.apply()
    }

    fun fetchSelectedLoan(): LoanData? {
        val gson = Gson()
        val json = prefs.getString(SELECTED_LOAN,"")
        return gson.fromJson(json,LoanData::class.java)
    }
    fun fetchSelectedLoanStrategy(): StrategyData? {
        val gson = Gson()
        val json = prefs.getString(SELECTED_STRAT,"")
        return gson.fromJson(json,StrategyData::class.java)
    }

    fun fetchSelectedArticle(): Article {
        val gson = Gson()
        val json = prefs.getString(SELECTED_ARTICLE, "")
        return gson.fromJson(json, Article::class.java)
    }

    fun saveAppTheme(theme:Boolean){
        val editor = prefs.edit()
        editor.putBoolean(APP_THEME,theme)
        editor.apply()
    }

    fun getAppTheme(): Boolean {
        return prefs.getBoolean(APP_THEME,false)
    }


}