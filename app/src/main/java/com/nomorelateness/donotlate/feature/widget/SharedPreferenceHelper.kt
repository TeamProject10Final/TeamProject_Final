package com.nomorelateness.donotlate.feature.widget

import android.content.Context
import com.google.gson.Gson
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

object SharedPreferencesHelper {
    private const val PREFS_NAME = "com.nomorelateness.donotlate"
    private const val KEY_PROMISE = "key_promise"

    fun savePromise(context: Context, promise: PromiseModel) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val promiseJson = Gson().toJson(promise)
        editor.putString(KEY_PROMISE, promiseJson)
        editor.apply()
    }

    fun getPromise(context: Context): PromiseModel? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val promiseJson = sharedPreferences.getString(KEY_PROMISE, null)
        return if (promiseJson != null) {
            Gson().fromJson(promiseJson, PromiseModel::class.java)
        } else {
            null
        }
    }
}
