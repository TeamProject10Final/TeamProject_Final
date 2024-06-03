package com.example.donotlate

import com.example.donotlate.consumption.presentation.SharedViewModelFactory
import com.example.donotlate.consumption.data.repository.ConsumptionRepositoryImpl
import com.example.donotlate.consumption.domain.repository.ConsumptionRepository
import com.example.donotlate.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.example.donotlate.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.example.donotlate.consumption.domain.usecase.InsertConsumptionUseCase

class AppContainer {

    val consumptionRepository: ConsumptionRepository by lazy {
        ConsumptionRepositoryImpl(DoNotLateApplication.getInstance())
    }

    var calculationContainer: CalculationContainer? = null

    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase by lazy {
        GetFinishedConsumptionUseCase(consumptionRepository)
    }

    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase by lazy {
        GetUnfinishedConsumptionUseCase(consumptionRepository)
    }

    val insertConsumptionUseCase: InsertConsumptionUseCase by lazy {
        InsertConsumptionUseCase(consumptionRepository)
    }
}

class CalculationContainer(
    private val consumptionRepository: ConsumptionRepository,
//    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
//    val insertConsumptionUseCase: InsertConsumptionUseCase
) {
    val sharedViewModelFactory = SharedViewModelFactory(
        consumptionRepository,
//        getFinishedConsumptionUseCase,
//        getUnfinishedConsumptionUseCase,
//        insertConsumptionUseCase
    )
}