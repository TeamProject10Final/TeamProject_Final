package com.nomorelateness.donotlate.feature.consumption.domain.entity

data class ConsumptionEntity(
    val historyId: Int,
    val detail: String,
    val date: String,
    val category: String,
    val total: String,
    val isPenalty: Boolean = false,
    val penalty: String?,
    val number: String,
    val price: Int = 0,
    val isFinished: Boolean = false
)