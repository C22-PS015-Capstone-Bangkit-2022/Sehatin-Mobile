package com.app.sehatin.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val TIME_FORMAT = "HH:mm aaa"
private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"

fun String.convertToDate(): String {
    return try {
        val inputFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
        val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.US)
        val outputFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL)
        val date = inputFormat.parse(this) as Date
        outputFormat.format(date) + " " + timeFormat.format(date)
    } catch (e: Exception) {
        this
    }
}

object DateHelper {
    fun getCurrentDate(): String {
        val timezone = Calendar.getInstance().timeZone
        val zonedTime = ZonedDateTime.now(timezone.toZoneId())
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return zonedTime.format(formatter)
    }
    fun getCurrentDateAfterMinutes(minute: Long): String {
        val timezone = Calendar.getInstance().timeZone
        val currentDate = getCurrentDate()
        val currentDateRemoveTimeZone = currentDate.removeRange(18, currentDate.length - 1)
        val zonedDate = LocalDateTime.parse(currentDateRemoveTimeZone).atZone(ZoneId.of(timezone.toZoneId().id)).plusMinutes(minute)
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return zonedDate.format(formatter)
    }
}