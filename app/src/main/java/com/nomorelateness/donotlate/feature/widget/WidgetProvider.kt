package com.nomorelateness.donotlate.feature.widget


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.google.gson.Gson
import com.nomorelateness.donotlate.MainActivity
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

//import com.nomorelateness.donotlate.feature.mypromise.PromiseModel
//import com.nomorelateness.donotlate.feature.mypromise.MyPromiseRoomActivity

class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val promiseModel = SharedPreferencesHelper.getPromise(context)
            if (promiseModel != null) {
                updateAppWidget(context, appWidgetManager, appWidgetId, promiseModel)
            }
        }
    }


    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            promiseModel: PromiseModel
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
                setTextViewText(
                    R.id.tv_promise_time,
                    promiseModel.promiseDate + "  " + promiseModel.promiseTime
                )
                setTextViewText(R.id.tv_promise_title, promiseModel.roomTitle)
                setTextViewText(R.id.tv_promise_destination, promiseModel.destination)

                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("promiseRoom", Gson().toJson(promiseModel))
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }
}

//data class WidgetPromiseModel(
//    var time: String,
//    var title: String
//)