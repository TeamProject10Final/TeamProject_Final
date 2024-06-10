package com.example.donotlate

import com.example.donotlate.feature.consumption.presentation.SharedViewModelFactory
import com.example.donotlate.feature.consumption.data.repository.ConsumptionRepositoryImpl
import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import com.example.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetLiveDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.ToggleIsFinishedUseCase
import com.example.donotlate.feature.consumption.presentation.ConsumptionViewModelFactory

class AppContainer {

    val consumptionRepository: ConsumptionRepository by lazy {
        ConsumptionRepositoryImpl(DoNotLateApplication.getInstance())
    }

    var calculationContainer: CalculationContainer? = null

    var consumptionContainer: ConsumptionContainer?=null

    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase by lazy {
        GetFinishedConsumptionUseCase(consumptionRepository)
    }

    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase by lazy {
        GetUnfinishedConsumptionUseCase(consumptionRepository)
    }

    val insertConsumptionUseCase: InsertConsumptionUseCase by lazy {
        InsertConsumptionUseCase(consumptionRepository)
    }

    val deleteConsumptionUseCase: DeleteConsumptionUseCase by lazy {
        DeleteConsumptionUseCase(consumptionRepository)
    }

    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase by lazy {
        GetConsumptionByCategoryUseCase(consumptionRepository)
    }

    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase by lazy {
        GetConsumptionByIdUseCase(consumptionRepository)
    }

    val getConsumptionDataUseCase: GetConsumptionDataUseCase by lazy {
        GetConsumptionDataUseCase(consumptionRepository)
    }

    val getTotalPriceUseCase: GetTotalPriceUseCase by lazy {
        GetTotalPriceUseCase(consumptionRepository)
    }
    val getDataCountUseCase: GetDataCountUseCase by lazy {
        GetDataCountUseCase(consumptionRepository)
    }
    val getLiveDataCountUseCase: GetLiveDataCountUseCase by lazy {
        GetLiveDataCountUseCase(consumptionRepository)
    }

    val toggleIsFinishedUseCase: ToggleIsFinishedUseCase by lazy {
        ToggleIsFinishedUseCase(consumptionRepository)
    }
}


class CalculationContainer(
//    private val consumptionRepository: ConsumptionRepository,
//    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    val insertConsumptionUseCase: InsertConsumptionUseCase,
    val deleteConsumptionUseCase: DeleteConsumptionUseCase,
//    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
//    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
//    val getConsumptionDataUseCase: GetConsumptionDataUseCase,
//    val getTotalPriceUseCase: GetTotalPriceUseCase,
    val getDataCountUseCase: GetDataCountUseCase
) {
    val sharedViewModelFactory = SharedViewModelFactory(
//        consumptionRepository,
//        getFinishedConsumptionUseCase,
//        getUnfinishedConsumptionUseCase,
        insertConsumptionUseCase,
        deleteConsumptionUseCase,
//        getConsumptionByCategoryUseCase,
//        getConsumptionByIdUseCase,
//        getConsumptionDataUseCase,
//        getTotalPriceUseCase,
        getDataCountUseCase
    )
}

class ConsumptionContainer(
    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    val insertConsumptionUseCase: InsertConsumptionUseCase,
    val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
    val getConsumptionDataUseCase: GetConsumptionDataUseCase,
    val getTotalPriceUseCase: GetTotalPriceUseCase,
    val getDataCountUseCase: GetDataCountUseCase,
    val getLiveDataCountUseCase: GetLiveDataCountUseCase,
    val toggleIsFinishedUseCase: ToggleIsFinishedUseCase
){
    val consumptionViewModelFactory = ConsumptionViewModelFactory(
        getFinishedConsumptionUseCase,
        getUnfinishedConsumptionUseCase,
        insertConsumptionUseCase,
        deleteConsumptionUseCase,
        getConsumptionByCategoryUseCase,
        getConsumptionByIdUseCase,
        getConsumptionDataUseCase,
        getTotalPriceUseCase,
        getDataCountUseCase,
        getLiveDataCountUseCase,
        toggleIsFinishedUseCase
    )
}