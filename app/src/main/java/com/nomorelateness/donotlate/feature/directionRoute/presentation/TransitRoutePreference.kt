package com.nomorelateness.donotlate.feature.directionRoute.presentation

data class TransitRoutePreference(
    val type: TransitRoutePreferenceEnum,
    val key: String,
    val message: String
)
