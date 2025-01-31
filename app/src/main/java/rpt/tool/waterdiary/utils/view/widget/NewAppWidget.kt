package rpt.tool.waterdiary.utils.view.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.RemoteViews
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.SelectBottleActivity
import rpt.tool.waterdiary.SplashScreenActivity
import rpt.tool.waterdiary.utils.helpers.DateHelper
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


class NewAppWidget : AppWidgetProvider() {


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
        var drink_water: Float = 0f

        fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            mContext = context
            //CharSequence widgetText = context.getString(R.string.appwidget_text);
            // Construct the RemoteViews object
            val views: RemoteViews = RemoteViews(context.packageName, R.layout.new_app_widget)
            views.setTextViewText(R.id.appwidget_text, get_today_report())

            views.setInt(R.id.circularProgressbar, "setMax", AppUtils.DAILY_WATER_VALUE as Int)
            views.setInt(R.id.circularProgressbar, "setProgress", drink_water.toInt())

            val launchMain = Intent(context, SplashScreenActivity::class.java)
            launchMain.putExtra("from_widget", true)
            launchMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingMainIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, launchMain, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget, pendingMainIntent)


            val launchMain2 = Intent(
                context,
                SelectBottleActivity::class.java
            )
            launchMain2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingMainIntent2: PendingIntent =
                PendingIntent.getActivity(context, 0, launchMain2, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.add_water, pendingMainIntent2)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        @SuppressLint("WrongConstant")
        fun get_today_report(): String {

            AppUtils.SDB=mContext!!.openOrCreateDatabase(
                AppUtils.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null)

            val dth: DateHelper = DateHelper()

            val arr_data = getdata(
                "tbl_drink_details",
                ("DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy")).toString() + "'"
            )

            if (SharedPreferencesManager.dailyWater == 0f) {
                AppUtils.DAILY_WATER_VALUE = 2500f
            } else {
                AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
            }

            if (check_blank_data("" + SharedPreferencesManager.waterUnit)) {
                AppUtils.WATER_UNIT_VALUE = "ml"
            } else {
                AppUtils.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit.toString()
            }

            drink_water = 0f
            for (k in arr_data.indices) {
                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)){
                    val x = arr_data[k]["ContainerValue"]!!.toFloat()
                    drink_water += x
                }
                else{
                    val x = arr_data[k]["ContainerValueOZ"]!!.toFloat()
                    drink_water += x
                }
            }

            return "" + (drink_water).toInt() + "/" +
                    AppUtils.DAILY_WATER_VALUE + " " + AppUtils.WATER_UNIT_VALUE

        }

        private fun check_blank_data(data: String): Boolean {
            return data == "" || data.isEmpty() || data.isEmpty() || data == "null"
        }

        @SuppressLint("Recycle")
        fun getdata(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
            val maplist = ArrayList<HashMap<String, String>>()

            var query = "SELECT * FROM $table_name"

            query += " where $where_con"

            val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

            println("SELECT QUERY : $query")

            if (c.moveToFirst()) {
                do {
                    val map = HashMap<String, String>()
                    for (i in 0 until c.columnCount) {
                        map[c.getColumnName(i)] = c.getString(i)
                    }

                    maplist.add(map)
                } while (c.moveToNext())
            }

            return maplist
        }
    }
}