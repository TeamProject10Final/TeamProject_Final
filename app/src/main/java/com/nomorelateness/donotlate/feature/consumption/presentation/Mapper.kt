package com.nomorelateness.donotlate.feature.consumption.presentation

import com.example.donotlate.feature.consumption.domain.entity.ConsumptionEntity

fun ConsumptionEntity.toModel() = ConsumptionModel(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)

fun ConsumptionModel.toEntity() = ConsumptionEntity(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)