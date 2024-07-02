package com.nomorelateness.donotlate.feature.widget

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

//import com.nomorelateness.donotlate.model.PromiseModel

object WidgetShared {

    private const val PREFERENCES_NAME = "widget_preferences"
    private const val KEY_CLOSEST_PROMISE = "closest_promise"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveClosestPromise(context: Context, promise: PromiseModel) {
        val json = Gson().toJson(promise)
        getPreferences(context).edit().putString(KEY_CLOSEST_PROMISE, json).apply()
    }

    fun getClosestPromise(context: Context): PromiseModel? {
        val json = getPreferences(context).getString(KEY_CLOSEST_PROMISE, null)
        return if (json != null) {
            Gson().fromJson(json, PromiseModel::class.java)
        } else {
            null
        }
    }

    fun clearClosestPromise(context: Context) {
        getPreferences(context).edit().remove(KEY_CLOSEST_PROMISE).apply()
    }
}

