package rpt.tool.waterdiary.utils.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.data.backuprestore.AlarmDetails
import rpt.tool.waterdiary.data.backuprestore.AlarmSubDetails
import rpt.tool.waterdiary.data.backuprestore.BackupRestore
import rpt.tool.waterdiary.data.backuprestore.ContainerDetails
import rpt.tool.waterdiary.data.backuprestore.DrinkDetails
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


internal class BackupHelper(private val mContext: Context) {
    var dth: DateHelper = DateHelper()


    fun createAutoBackSetup() {
        if (!SharedPreferencesManager.autoBackUp) return

        checkStoragePermissions()
    }

    private fun checkStoragePermissions() {
        backup_data()
    }

    private fun backup_data() {
        var arr_data: ArrayList<HashMap<String, String>> = getdata("tbl_container_details")

        val backupRestore: BackupRestore = BackupRestore()

        val containerDetailsList: MutableList<ContainerDetails> = ArrayList<ContainerDetails>()

        for (k in arr_data.indices) {
            val containerDetails: ContainerDetails = ContainerDetails()
            containerDetails.containerID = arr_data[k]["ContainerID"] ?: ""
            containerDetails.containerMeasure = arr_data[k]["ContainerMeasure"] ?: ""
            containerDetails.containerValue = arr_data[k]["ContainerValue"] ?: ""
            containerDetails.containerValueOZ = arr_data[k]["ContainerValueOZ"] ?: ""
            containerDetails.isOpen = arr_data[k]["IsOpen"] ?: ""
            containerDetails.id = arr_data[k]["id"] ?: ""
            containerDetails.isCustom = arr_data[k]["IsCustom"] ?: ""
            containerDetailsList.add(containerDetails)
        }

        arr_data = getdata("tbl_drink_details")

        val drinkDetailsList: MutableList<DrinkDetails> = ArrayList<DrinkDetails>()

        for (k in arr_data.indices) {
            val drinkDetails: DrinkDetails = DrinkDetails()
            drinkDetails.drinkDateTime = arr_data[k]["DrinkDateTime"] ?: ""
            drinkDetails.drinkDate = arr_data[k]["DrinkDate"] ?: ""
            drinkDetails.drinkTime = arr_data[k]["DrinkTime"] ?: ""
            drinkDetails.containerMeasure = arr_data[k]["ContainerMeasure"] ?: ""
            drinkDetails.containerValue = arr_data[k]["ContainerValue"] ?: ""
            drinkDetails.containerValueOZ = arr_data[k]["ContainerValueOZ"] ?: ""
            drinkDetails.id = arr_data[k]["id"] ?: ""
            drinkDetails.todayGoal = arr_data[k]["TodayGoal"] ?: ""
            drinkDetails.todayGoalOZ = arr_data[k]["TodayGoalOZ"] ?: ""
            drinkDetailsList.add(drinkDetails)
        }

        arr_data = getdata("tbl_alarm_details")

        val alarmDetailsList: MutableList<AlarmDetails> = ArrayList<AlarmDetails>()

        for (k in arr_data.indices) {
            val alarmDetails: AlarmDetails = AlarmDetails()
            alarmDetails.alarmId = arr_data[k]["AlarmId"] ?: ""
            alarmDetails.alarmInterval = arr_data[k]["AlarmInterval"] ?: ""
            alarmDetails.alarmTime = arr_data[k]["AlarmTime"] ?: ""
            alarmDetails.alarmType = arr_data[k]["AlarmType"] ?: ""
            alarmDetails.id = arr_data[k]["id"] ?: ""

            alarmDetails.alarmSundayId = arr_data[k]["SundayAlarmId"] ?: ""
            alarmDetails.alarmMondayId = arr_data[k]["MondayAlarmId"] ?: ""
            alarmDetails.alarmTuesdayId = arr_data[k]["TuesdayAlarmId"] ?: ""
            alarmDetails.alarmWednesdayId = arr_data[k]["WednesdayAlarmId"] ?: ""
            alarmDetails.alarmThursdayId = arr_data[k]["ThursdayAlarmId"] ?: ""
            alarmDetails.alarmFridayId = arr_data[k]["FridayAlarmId"] ?: ""
            alarmDetails.alarmSaturdayId = arr_data[k]["SaturdayAlarmId"] ?: ""

            alarmDetails.isOff = arr_data[k]["IsOff"]!!.toInt()
            alarmDetails.sunday = arr_data[k]["Sunday"]!!.toInt()
            alarmDetails.monday = arr_data[k]["Monday"]!!.toInt()
            alarmDetails.tuesday = arr_data[k]["Tuesday"]!!.toInt()
            alarmDetails.wednesday = arr_data[k]["Wednesday"]!!.toInt()
            alarmDetails.thursday = arr_data[k]["Thursday"]!!.toInt()
            alarmDetails.friday = arr_data[k]["Friday"]!!.toInt()
            alarmDetails.saturday = arr_data[k]["Saturday"]!!.toInt()


            val alarmSubDetailsList: MutableList<AlarmSubDetails> = ArrayList<AlarmSubDetails>()

            val arr_data2 = getdata("tbl_alarm_sub_details",
                ("SuperId=" + arr_data[k]["id"]) ?: ""
            )

            d("arr_data2 : ", "" + arr_data2.size)

            for (j in arr_data2.indices) {
                val alarmSubDetails: AlarmSubDetails = AlarmSubDetails()
                alarmSubDetails.alarmId = arr_data2[j]["AlarmId"]
                alarmSubDetails.alarmTime = arr_data2[j]["AlarmTime"]
                alarmSubDetails.id = arr_data2[j]["id"]
                alarmSubDetails.superId = arr_data2[j]["SuperId"]
                alarmSubDetailsList.add(alarmSubDetails)
            }

            alarmDetails.alarmSubDetails = alarmSubDetailsList

            alarmDetailsList.add(alarmDetails)
        }


        backupRestore.containerDetails = containerDetailsList
        backupRestore.drinkDetails = drinkDetailsList
        backupRestore.alarmDetails = alarmDetailsList

        backupRestore.totalDrink = SharedPreferencesManager.dailyWater

        backupRestore.totalWeight = SharedPreferencesManager.personWeight
        backupRestore.totalHeight = SharedPreferencesManager.personHeight

        backupRestore.isCMUnit(SharedPreferencesManager.personHeightUnit)
        backupRestore.isKgUnit(SharedPreferencesManager.personWeightUnit)
        backupRestore.isMlUnit(SharedPreferencesManager.personWeightUnit)


        backupRestore.reminderOption = SharedPreferencesManager.reminderOpt
        backupRestore.reminderSound = SharedPreferencesManager.reminderSound
        backupRestore.isDisableNotifiction(SharedPreferencesManager.disableNotification)
        backupRestore.isManualReminderActive(SharedPreferencesManager.isManualReminder)
        backupRestore.isReminderVibrate(SharedPreferencesManager.reminderVibrate)

        backupRestore.userName = SharedPreferencesManager.userName
        backupRestore.userGender = SharedPreferencesManager.userGender


        backupRestore.isDisableSound(SharedPreferencesManager.disableSoundWhenAddWater)


        backupRestore.isAutoBackup(SharedPreferencesManager.autoBackUp)
        backupRestore.autoBackupType = SharedPreferencesManager.autoBackUpType
        backupRestore.setAutoBackupID(SharedPreferencesManager.autoBackUpId)


        val jsondata = Gson().toJson(backupRestore)
        val jsonParser1 = JsonParser()
        val jsonObject = jsonParser1.parse(jsondata) as JsonObject

        store_response(jsonObject.toString())
    }

    private fun store_response(plainBody: String?) {
        val f = File(
            (Environment.getExternalStorageDirectory()
                .toString() + "/" + AppUtils.APP_DIRECTORY_NAME).toString() + "/"
        )

        if (!f.exists()) f.mkdir()

        if (f.exists()) {
            val dt: String = dth.getCurrentDate("dd-MMM-yyyy hh:mm:ss a")

            val full_file_name = ((Environment.getExternalStorageDirectory()
                .toString() + "/" + AppUtils.APP_DIRECTORY_NAME).toString() + "/Backup_"
                    + dt + ".txt")

            val file = File(full_file_name)

            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (ioe: IOException) {
                    ioe.message?.let { e(Throwable(ioe), it) }
                    ioe.printStackTrace()
                }
            }

            try {
                val fileOutputStream = FileOutputStream(file)
                val writer = OutputStreamWriter(fileOutputStream)
                writer.append(plainBody)
                writer.close()
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.message?.let { e(Throwable(e), it) }
                e.printStackTrace()
            } catch (e: IOException) {
                e.message?.let { e(Throwable(e), it) }
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("Recycle")
    fun getdata(table_name: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val query = "SELECT * FROM $table_name"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

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