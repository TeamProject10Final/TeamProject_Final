package com.nomorelateness.donotlate.feature.widget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

//import com.nomorelateness.donotlate.model.Promise
class WidgetViewModel : ViewModel() {
    private val _closestPromise = MutableLiveData<PromiseModel?>()
    val closestPromise: LiveData<PromiseModel?> get() = _closestPromise

    private val _distanceBetweenDouble = MutableLiveData<Double>(0.0)
    val distanceBetweenDouble: LiveData<Double> get() = _distanceBetweenDouble

    fun updateClosestPromise(promise: PromiseModel?) {
        _closestPromise.value = promise
    }

    fun updateDistanceBetweenDouble(distance: Double) {
        _distanceBetweenDouble.value = distance
    }

}