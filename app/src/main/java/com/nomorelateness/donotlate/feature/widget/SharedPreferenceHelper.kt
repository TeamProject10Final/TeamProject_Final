package com.nomorelateness.donotlate.feature.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

        // 위젯 업데이트 트리거
        triggerWidgetUpdate(context)
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

    private fun triggerWidgetUpdate(context: Context) {
        val intent = Intent(context, WidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, WidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }

    fun updateHasArrived(context: Context, userId: String, hasArrived: Boolean) {
        val currentPromise = getPromise(context)
        if (currentPromise != null) {
            val updatedHasArrived = currentPromise.hasArrived.toMutableMap()
            updatedHasArrived[userId] = hasArrived
            val updatedPromise = currentPromise.copy(hasArrived = updatedHasArrived)
            savePromise(context, updatedPromise)
        }
    }

    fun updateHasDeparture(context: Context, userId: String, hasDeparture: Boolean) {
        val currentPromise = getPromise(context)
        if (currentPromise != null) {
            val updatedHasDeparture = currentPromise.hasDeparture.toMutableMap()
            updatedHasDeparture[userId] = hasDeparture
            val updatedPromise = currentPromise.copy(hasDeparture = updatedHasDeparture)
            savePromise(context, updatedPromise)
        }
    }
}
