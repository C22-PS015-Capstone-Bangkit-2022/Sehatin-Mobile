package com.app.sehatin.utils

import java.text.NumberFormat
import java.util.*

fun String.capital() : String {
    var output = ""
    val words = this.split(" ")
    for(word in words) {
        output += word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + " "
    }
    return output.trim()
}

fun Long.toCurrencyFormat(): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    numberFormat.minimumFractionDigits = 0
    return numberFormat.format(this)
}