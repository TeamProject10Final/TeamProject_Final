package com.nomorelateness.donotlate.core.presentation

import com.nomorelateness.donotlate.feature.main.presentation.model.UserModel


object CurrentUser {
    var userData: UserModel? = null

    fun clearData(){
        userData = null
    }
}