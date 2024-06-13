package com.example.donotlate.core.util

import android.view.MotionEvent
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toFormattedString(): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val date = this.toDate()
    return sdf.format(date)
}