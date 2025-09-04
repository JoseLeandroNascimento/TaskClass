package com.example.taskclass.common.validators

import com.example.taskclass.common.data.Validator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeValidator(
    val timeMin: String? = null,
    val timeMax: String? = null,
    val timeFormattedError: String = "Hora inválida",
    val timeRangeError: String = "Hora fora do intervalo permitido"
) : Validator<String?> {

    private val formatter = SimpleDateFormat("HH:mm", Locale.US).apply {
        isLenient = false
    }

    private fun parseTime(value: String): Date? {
        return try {
            formatter.parse(value)
        } catch (e: ParseException) {
            null
        }
    }

    override fun execute(value: String?): String? {

        if (value == null)
            return timeFormattedError

        val time = parseTime(value) ?: return timeFormattedError

        if (!TIME_REGEX.matches(value)) return timeFormattedError

        timeMin?.let {
            val min = parseTime(it) ?: return timeFormattedError
            if (time.before(min)) return timeRangeError
        }

        timeMax?.let {
            val max = parseTime(it) ?: return timeFormattedError
            if (time.after(max)) return timeRangeError
        }


        return null

    }

    companion object {
        private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
    }
}