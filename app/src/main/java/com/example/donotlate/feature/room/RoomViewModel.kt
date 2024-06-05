package com.example.donotlate.feature.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoomViewModel : ViewModel() {

    private val inputText: MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> = inputText

    fun updateText(input: String) {
        inputText.value = input
    }
}