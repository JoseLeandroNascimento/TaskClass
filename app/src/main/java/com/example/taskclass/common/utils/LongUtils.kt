package com.example.taskclass.common.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * @author José Leandro da Silva Nascimento
 *
 * Long tem que representar o tempo em milisegundos.
 */
fun Long.formatRelativeOrDate(): String {
    if (this <= 0L) return ""
    val cal = Calendar.getInstance()
    val todayY = cal.get(Calendar.YEAR)
    val todayD = cal.get(Calendar.DAY_OF_YEAR)

    cal.timeInMillis = this
    val y = cal.get(Calendar.YEAR)
    val d = cal.get(Calendar.DAY_OF_YEAR)

    val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return when {
        y == todayY && d == todayD -> "Hoje, ${timeFmt.format(Date(this))}"
        y == todayY && d == todayD - 1 -> "Ontem, ${timeFmt.format(Date(this))}"
        else -> dateFmt.format(Date(this))
    }
}