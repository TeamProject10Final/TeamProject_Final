package com.example.donotlate.feature.main.presentation.model

sealed class Result<out T> {
    data class Success<out T>(val data:T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}