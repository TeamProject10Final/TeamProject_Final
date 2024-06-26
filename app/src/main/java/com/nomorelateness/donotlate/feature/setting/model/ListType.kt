package com.nomorelateness.donotlate.feature.setting.model

const val SettingListItemType1 = 1
const val SettingListItemType2 = 2

data class ListType(
    val title : String,
    val type : Int
)
