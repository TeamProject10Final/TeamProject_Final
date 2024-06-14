package com.example.donotlate.feature.mypromise.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase

class MyPromiseViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase
) : ViewModel() {

}

class MyPromiseViewModelFactory(private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseViewModel(
                loadToMyPromiseListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
