package rpt.tool.waterdiary.utils.helpers

import android.app.Activity
import android.content.ContentValues
import android.content.Context


class DBHelper
    (var mContext: Context, var act: Activity) {
    var dh: DatabaseHelper = DatabaseHelper(mContext, act)

    init {
        createTable()
    }

    fun createTable() {
        val enum_fields = HashMap<String, String>()
        enum_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        enum_fields["ContainerID"] = "INTEGER DEFAULT 0"
        enum_fields["ContainerValue"] = "TEXT"
        enum_fields["ContainerValueOZ"] = "TEXT"
        enum_fields["ContainerMeasure"] = "TEXT"
        enum_fields["IsOpen"] = "TEXT"
        enum_fields["IsCustom"] = "INTEGER DEFAULT 0"
        dh.createTable("tbl_container_details", enum_fields)
        enum_fields.clear()

        add_container_data()

        val item_fields = HashMap<String, String>()
        item_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        item_fields["ContainerValue"] = "TEXT"
        item_fields["ContainerValueOZ"] = "TEXT"
        item_fields["ContainerMeasure"] = "TEXT"
        item_fields["DrinkDate"] = "TEXT"
        item_fields["DrinkTime"] = "TEXT"
        item_fields["DrinkDateTime"] = "TEXT"
        item_fields["TodayGoal"] = "TEXT"
        item_fields["TodayGoalOZ"] = "TEXT"
        dh.createTable("tbl_drink_details", item_fields)
        item_fields.clear()


        val alarm_fields = HashMap<String, String>()
        alarm_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        alarm_fields["AlarmTime"] = "TEXT"
        alarm_fields["AlarmId"] = "TEXT"
        alarm_fields["AlarmType"] = "TEXT"
        alarm_fields["AlarmInterval"] = "TEXT"

        alarm_fields["IsOff"] = "INTEGER DEFAULT 0"
        alarm_fields["Sunday"] = "INTEGER DEFAULT 1"
        alarm_fields["Monday"] = "INTEGER DEFAULT 1"
        alarm_fields["Tuesday"] = "INTEGER DEFAULT 1"
        alarm_fields["Wednesday"] = "INTEGER DEFAULT 1"
        alarm_fields["Thursday"] = "INTEGER DEFAULT 1"
        alarm_fields["Friday"] = "INTEGER DEFAULT 1"
        alarm_fields["Saturday"] = "INTEGER DEFAULT 1"
        alarm_fields["SundayAlarmId"] = "TEXT"
        alarm_fields["MondayAlarmId"] = "TEXT"
        alarm_fields["TuesdayAlarmId"] = "TEXT"
        alarm_fields["WednesdayAlarmId"] = "TEXT"
        alarm_fields["ThursdayAlarmId"] = "TEXT"
        alarm_fields["FridayAlarmId"] = "TEXT"
        alarm_fields["SaturdayAlarmId"] = "TEXT"

        dh.createTable("tbl_alarm_details", alarm_fields)
        alarm_fields.clear()

        val alarm_sub_fields = HashMap<String, String>()
        alarm_sub_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        alarm_sub_fields["AlarmTime"] = "TEXT"
        alarm_sub_fields["AlarmId"] = "TEXT"
        alarm_sub_fields["SuperId"] = "TEXT"
        dh.createTable("tbl_alarm_sub_details", alarm_sub_fields)
        alarm_sub_fields.clear()

        val reached_fields = HashMap<String, String>()
        reached_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        reached_fields["ContainerValue"] = "TEXT"
        reached_fields["ContainerValueOZ"] = "TEXT"
        reached_fields["DrinkDate"] = "TEXT"
        dh.createTable("tbl_reached_goal_details", reached_fields)
        reached_fields.clear()

        val avis_fields = HashMap<String, String>()
        avis_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        avis_fields["DonorDate"] = "TEXT"
        dh.createTable("tbl_blood_donor_details", avis_fields)
        avis_fields.clear()

        val container_clicked_fields = HashMap<String, String>()
        container_clicked_fields["id"] = "INTEGER PRIMARY KEY AUTOINCREMENT"
        container_clicked_fields["ContainerValue"] = "TEXT"
        container_clicked_fields["ContainerValueOZ"] = "TEXT"
        container_clicked_fields["DrinkDate"] = "TEXT"
        container_clicked_fields["Quantity"] = "TEXT"
        dh.createTable("tbl_container_clicked_details", container_clicked_fields)
        container_clicked_fields.clear()
    }

    fun add_container_data() {
        if (dh.totalRow("tbl_container_details") > 0) return

        val cval = arrayOf(50, 100, 150, 200, 250, 300, 500, 600, 700, 800, 900, 1000)
        val cval2 = arrayOf(2, 3, 5, 7, 8, 10, 17, 20, 24, 27, 30, 34)
        val iop = arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0)

        for (k in cval.indices) {
            val initialValues = ContentValues()

            initialValues.put("ContainerID", "" + (k + 1))
            initialValues.put("ContainerValue", "" + cval[k])
            initialValues.put("ContainerValueOZ", "" + cval2[k])
            initialValues.put("IsOpen", "" + iop[k])

            dh.insert("tbl_container_details", initialValues)
        }
    }

}