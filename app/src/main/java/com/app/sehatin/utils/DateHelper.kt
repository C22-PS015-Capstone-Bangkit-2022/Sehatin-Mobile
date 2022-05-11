package com.app.sehatin.utils

import java.text.SimpleDateFormat
import java.util.*

const val DATE_INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val TIME_FORMAT = "HH:mm aaa"

fun String.convertToDate(): String {
    val inputFormat = SimpleDateFormat(DATE_INPUT_PATTERN, Locale.US)
    val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.US)
    val outputFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL)
    val date = inputFormat.parse(this) as Date
    return outputFormat.format(date) + " " + timeFormat.format(date)
}