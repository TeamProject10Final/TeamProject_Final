package com.example.donotlate.core.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toFormattedString(): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val date = this.toDate()
    return sdf.format(date)
}

fun Timestamp.toFormattedHMString(): String {
    val sdf = SimpleDateFormat("a hh:mm", Locale.KOREAN)
    val hours = this.toDate()
    return sdf.format(hours)
}