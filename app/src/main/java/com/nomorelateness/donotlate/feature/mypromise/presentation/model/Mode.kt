package com.nomorelateness.donotlate.feature.mypromise.presentation.model

enum class Mode(val displayName: String, val key: String) {
    OPTION_1("대중교통", "transit"),
    OPTION_2("자동차", "driving"),
    OPTION_3("도보", "walking"),
    OPTION_4("자전거", "bicycling");

    companion object {
        fun fromDisplayName(displayName: String): Mode? {
            return entries.firstOrNull { it.displayName == displayName }
        }

        fun fromKey(key: String): Mode? {
            return entries.firstOrNull { it.key == key }
        }
    }
}