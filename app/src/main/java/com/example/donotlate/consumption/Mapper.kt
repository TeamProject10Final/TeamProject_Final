package com.example.donotlate.consumption

import com.example.donotlate.consumption.domain.entity.ConsumptionEntity

fun ConsumptionEntity.toModel() = ConsumptionModel(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)

fun ConsumptionModel.toEntity() = ConsumptionEntity(
    historyId, detail, date, category, total, isPenalty, penalty, number, price, isFinished
)

class ConsumptionMapper {
    fun toModel(entity: ConsumptionEntity): ConsumptionModel {
        return ConsumptionModel(
            historyId = entity.historyId,
            detail = entity.detail,
            date = entity.date,
            category = entity.category,
            total = entity.total,
            isPenalty = entity.isPenalty,
            penalty = entity.penalty,
            number = entity.number,
            price = entity.price,
            isFinished = entity.isFinished
        )
    }

    fun toEntity(model: ConsumptionModel): ConsumptionEntity {
        return ConsumptionEntity(
            historyId = model.historyId,
            detail = model.detail,
            date = model.date,
            category = model.category,
            total = model.total,
            isPenalty = model.isPenalty,
            penalty = model.penalty,
            number = model.number,
            price = model.price,
            isFinished = model.isFinished
        )
    }

fun List<ConsumptionEntity>.toModelList(): List<ConsumptionModel> {
    return this.map { entity ->
        ConsumptionModel(
            entity.historyId,
            entity.detail,
            entity.date,
            entity.category,
            entity.total,
            entity.isPenalty,
            entity.penalty,
            entity.number,
            entity.price,
            entity.isFinished
        )
    }
}

