package com.nomorelateness.donotlate.feature.consumption.presentation

data class FirebaseConsumptionModel(
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

fun ConsumptionModel.toFirebaseConsumptionModel(): FirebaseConsumptionModel {
    return FirebaseConsumptionModel(
        historyId = this.historyId,
        detail = this.detail,
        date = this.date,
        category = this.category,
        total = this.total,
        penaltyFlag = this.isPenalty, // 필드 이름 변경
        penalty = this.penalty,
        number = this.number,
        price = this.price,
        hasFinished = this.isFinished
    )
}

fun FirebaseConsumptionModel.toRoomEntity(): ConsumptionModel {
    return ConsumptionModel(
        historyId = this.historyId,
        detail = this.detail,
        date = this.date,
        category = this.category,
        total = this.total,
        isPenalty = this.penaltyFlag, // 필드 이름 변경
        penalty = this.penalty,
        number = this.number,
        price = this.price,
        isFinished = this.hasFinished
    )
}