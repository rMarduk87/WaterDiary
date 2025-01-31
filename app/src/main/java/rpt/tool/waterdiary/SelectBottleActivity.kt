package rpt.tool.waterdiary

import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import rpt.tool.waterdiary.AppClose.Companion.exitApplication
import rpt.tool.waterdiary.base.MasterBaseActivity
import rpt.tool.waterdiary.data.model.Container
import rpt.tool.waterdiary.databinding.ActivitySelectBottleBinding
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.ozToMlConverter
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.widget.NewAppWidget


class SelectBottleActivity : MasterBaseActivity() {

    var containerArrayList: ArrayList<Container> = ArrayList()
    var selected_pos: Int = 0
    var drink_water: Float = 0f
    lateinit var binding: ActivitySelectBottleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBottleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setLayout(
            RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT
        )

        val notificationManager =
            act!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)

        if (SharedPreferencesManager.dailyWater == 0f) {
            AppUtils.DAILY_WATER_VALUE = 2500f
        } else {
            AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        if (sh!!.check_blank_data("" + SharedPreferencesManager.waterUnit)) {
            AppUtils.WATER_UNIT_VALUE = "ml"
        } else {
            AppUtils.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit.toString()
        }

        body()
    }


    private fun saveDefaultContainer() {
        if (!SharedPreferencesManager.hideWelcomeScreen) {
            SharedPreferencesManager.personWeightUnit = true
            SharedPreferencesManager.personWeightUnit = true
            SharedPreferencesManager.personWeight = "80"
            SharedPreferencesManager.userName = ""
            intent = Intent(act, InitUserInfoActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            var addRecord = true

            var str = ""
            str =
                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                    sh!!.get_string(R.string.str_you_should_not_drink_more_then_target)
                    .replace("$1", "8000 ml")
                else sh!!.get_string(R.string.str_you_should_not_drink_more_then_target)
                    .replace("$1", "270 fl oz")

            val arr_data: ArrayList<HashMap<String, String>> = dh!!.getdata(
                    "tbl_drink_details",
                    "DrinkDate ='" + dth!!.getCurrentDate(AppUtils.DATE_FORMAT) + "'"
                )

            drink_water = 0f
            for (k in arr_data.indices) {
                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)){
                    val x = arr_data[k]["ContainerValue"]!!.toFloat()
                    NewAppWidget.drink_water += x
                }
                else{
                    val x = arr_data[k]["ContainerValueOZ"]!!.toFloat()
                    NewAppWidget.drink_water += x
                }
            }

            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)
                && drink_water > 8000
            ) {
                ah!!.customAlert(str)
                addRecord = false
            } else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                && drink_water > 270
            ) {
                ah!!.customAlert(str)
                addRecord = false
            }


            var count_drink_after_add_current_water = drink_water

            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                count_drink_after_add_current_water +=
                    ("" + containerArrayList[selected_pos].containerValue).toFloat()
            else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true)))
                count_drink_after_add_current_water +=
                    ("" + containerArrayList[selected_pos].containerValueOZ).toFloat()

            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)
                && count_drink_after_add_current_water > 8000
            ) {
                if (drink_water >= 8000)
                    ah!!.customAlert(str)
                else if (AppUtils.DAILY_WATER_VALUE <
                    (8000 - ("" + containerArrayList[selected_pos].containerValue)
                        .toFloat()))
                    ah!!.customAlert(str)
            } else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                && count_drink_after_add_current_water > 270
            ) {
                if (drink_water >= 270)
                    ah!!.customAlert(str)
                else if (AppUtils.DAILY_WATER_VALUE <
                    (270 - ("" + containerArrayList[selected_pos].containerValueOZ).toFloat()))
                    ah!!.customAlert(str)
            }

            if (drink_water == 8000f && AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
                addRecord = false
            } else if (drink_water == 270f && !AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
                addRecord = false
            }

            if (addRecord) {
                val initialValues = ContentValues()

                initialValues.put(
                    "ContainerValue",
                    "" + containerArrayList[selected_pos].containerValue
                )
                initialValues.put(
                    "ContainerValueOZ",
                    "" + containerArrayList[selected_pos].containerValueOZ
                )
                initialValues.put("ContainerMeasure", "" + SharedPreferencesManager.waterUnit)
                initialValues.put("DrinkDate", "" + dth!!.getCurrentDate("dd-MM-yyyy"))
                initialValues.put("DrinkTime", "" + dth!!.getCurrentTime(true))
                initialValues.put("DrinkDateTime", "" + dth!!.getCurrentDate("dd-MM-yyyy HH:mm:ss"))

                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
                    initialValues.put("TodayGoal", "" + AppUtils.DAILY_WATER_VALUE)
                    initialValues.put(
                        "TodayGoalOZ",
                        "" + mlToOzConverter(AppUtils.DAILY_WATER_VALUE)
                    )
                } else {
                    initialValues.put(
                        "TodayGoal",
                        "" + ozToMlConverter(AppUtils.DAILY_WATER_VALUE)
                    )
                    initialValues.put("TodayGoalOZ", "" + AppUtils.DAILY_WATER_VALUE)
                }

                dh!!.insert(
                    "tbl_drink_details",
                    initialValues
                )
            }


            //==============================
            val intent = Intent(act, NewAppWidget::class.java)
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            val ids = AppWidgetManager.getInstance(act).getAppWidgetIds(
                ComponentName(
                    act!!,
                    NewAppWidget::class.java
                )
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            act!!.sendBroadcast(intent)

            exitApplication(mContext!!)
        }
    }

    private fun body() {
        var selected_container_id = "1"

        selected_container_id = if (SharedPreferencesManager.selectedContainer == 0) "1"
        else "" + SharedPreferencesManager.selectedContainer

        val arr_container: ArrayList<HashMap<String, String>> = dh!!.getdata(
                "tbl_container_details",
                "IsCustom",
                1
            )

        for (k in arr_container.indices) {
            val container = Container()
            container.containerId = arr_container[k]["ContainerID"]
            container.containerValue = arr_container[k]["ContainerValue"]
            container.containerValueOZ = arr_container[k]["ContainerValueOZ"]
            container.isOpen(
                if (arr_container[k]["IsOpen"].equals(
                        "1",
                        ignoreCase = true
                    )
                ) true else false
            )

            container.isSelected(
                if (selected_container_id.equals(
                        arr_container[k]["ContainerID"],
                        ignoreCase = true
                    )
                ) true else false
            )
            if (container.isSelected) selected_pos = k //+1

            containerArrayList.add(container)
        }

        saveDefaultContainer()
    }
}