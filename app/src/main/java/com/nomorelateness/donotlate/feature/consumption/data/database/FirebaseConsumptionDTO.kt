package com.nomorelateness.donotlate.feature.consumption.data.database

data class FirebaseConsumptionDTO(
    val historyId: String = "",
    val detail: String = "",
    val date: String = "",
    val category: String = "",
    val total: String = "",
    val penaltyFlag: Boolean = false, // 필드 이름 변경
    val penalty: String? = null,
    val number: String = "",
    val price: Int = 0,
    val hasFinished: Boolean = false
)