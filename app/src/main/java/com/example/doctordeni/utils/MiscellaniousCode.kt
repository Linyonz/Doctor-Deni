package com.example.doctordeni.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MiscellaniousCode {

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToLocalDate(dateString:String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return  LocalDate.parse(dateString, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeAgo(dateString: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val date = LocalDateTime.parse(dateString, formatter)

        val now = LocalDateTime.now()

        val years = ChronoUnit.YEARS.between(date, now)
        val months = ChronoUnit.MONTHS.between(date, now)
        val days = ChronoUnit.DAYS.between(date, now)
        val hours = ChronoUnit.HOURS.between(date, now)
        val minutes = ChronoUnit.MINUTES.between(date, now)
        val seconds = ChronoUnit.SECONDS.between(date, now)

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
            months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
        }
    }

    fun convertDurationToDate(days: Int = 0, weeks: Int = 0, months: Int = 0): String {
        val calendar = Calendar.getInstance()

        // Add the specified durations to the calendar
        calendar.add(Calendar.DAY_OF_YEAR, days)
        calendar.add(Calendar.WEEK_OF_YEAR, weeks)
        calendar.add(Calendar.MONTH, months)

        // Extract the years, months, and days from the calendar
        val years = calendar.get(Calendar.YEAR) - 1970 // Adjust for the epoch year
        val remainingMonths = calendar.get(Calendar.MONTH)
        val remainingDays = calendar.get(Calendar.DAY_OF_MONTH) - 1 // Adjust for the first day of the month

        // Return the date in the desired format
        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""}, $remainingMonths month${if (remainingMonths > 1) "s" else ""}, $remainingDays day${if (remainingDays > 1) "s" else ""}"
            months > 0 -> "$months month${if (months > 1) "s" else ""}, $remainingDays day${if (remainingDays > 1) "s" else ""}"
            else -> "$remainingDays day${if (remainingDays > 1) "s" else ""}"
        }
    }

    fun convertDaysToYearsMonthsWeeksDays(totalDays: Long): String {
        if (totalDays < 0) {
            return "Invalid input: totalDays must be non-negative."
        }

        // Define constants for calendar calculations
        val daysInYear = 365
        val daysInMonth = 30
        val daysInWeek = 7

        // Calculate years
        val years = totalDays / daysInYear
        var remainingDays = totalDays % daysInYear

        // Calculate months
        val months = remainingDays / daysInMonth
        remainingDays %= daysInMonth

        // Calculate weeks
        val weeks = remainingDays / daysInWeek
        remainingDays %= daysInWeek

        // Prepare the result string based on calculated values
        val result = StringBuilder()

        if (years > 0) {
            if(years < 2) {
                result.append("$years year ")
            } else{
                result.append("$years years ")
            }
        }
        if (months > 0) {
            if(months < 2){
                result.append("$months month ")
            }else {
                result.append("$months months ")
            }

        }
        if (weeks > 0) {
            if(weeks < 2){
                result.append("$weeks week ")
            } else {
                result.append("$weeks weeks ")
            }
        }
        if (remainingDays > 0) {
            if(remainingDays < 2){
                result.append("$remainingDays day")
            } else {
                result.append("$remainingDays days")
            }
        }

        return result.toString().trim()
    }

    fun formatNumberWithCommas(number: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        return formatter.format(number)
    }

    fun daysUntilLoan(loanDateText: String): Long? {
        try {


        val loanDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val loanDate = loanDateFormat.parse(loanDateText)

        val currentDate = Date()

        return daysBetween(currentDate, loanDate)
        } catch(e:Exception){
            Log.e("error",e.message.toString())
        }
        return null
    }

    fun daysBetween(date1: Date, date2: Date): Long {
        val date1Timestamp = date1.time
        val date2Timestamp = date2.time

        val differenceInMillis = date2Timestamp - date1Timestamp

        return differenceInMillis / (1000 * 60 * 60 * 24)
    }

}