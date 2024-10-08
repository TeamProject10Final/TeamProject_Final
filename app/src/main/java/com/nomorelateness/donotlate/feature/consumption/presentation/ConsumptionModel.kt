package com.nomorelateness.donotlate.feature.consumption.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConsumptionModel(
    val historyId: String,
    val detail: String,
    val date: String,
    val category: String,
    val total: String,
    val isPenalty: Boolean = false,
    val penalty: String?,
    val number: String,
    val price: Int = 0,
    val isFinished: Boolean = false
) : Parcelable