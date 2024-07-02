package com.nomorelateness.donotlate.feature.consumption.presentation

import com.nomorelateness.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity

fun ConsumptionEntity.toModel() = ConsumptionModel(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)

fun ConsumptionModel.toEntity() = ConsumptionEntity(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)

fun FirebaseConsumptionModel.toEntity() = FirebaseConsumptionEntity(
    historyId, detail, date, category, total, penaltyFlag, penalty, number, price, hasFinished
)

fun FirebaseConsumptionEntity.toModel() = FirebaseConsumptionModel(
    historyId, detail, date, category, total, penaltyFlag, penalty, number, price, hasFinished
)