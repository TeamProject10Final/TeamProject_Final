package com.example.donotlate.feature.minigame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RouletteViewModel : ViewModel() {
    private var inputText: MutableLiveData<List<String>> = MutableLiveData()
    fun getData(): MutableLiveData<List<String>> = inputText
    fun updateText(input: List<String>) {
        inputText.value = input
    }


}