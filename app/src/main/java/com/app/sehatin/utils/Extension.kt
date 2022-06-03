package com.app.sehatin.utils

import java.util.*

fun String.capital() : String {
    var output = ""
    val words = this.split(" ")
    for(word in words) {
        output += word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + " "
    }
    return output.trim()
}