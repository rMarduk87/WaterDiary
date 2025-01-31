package rpt.tool.waterdiary.receiver

import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


class BackupReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
        d(DEBUG_TAG, "Recurring alarm 2; requesting download service.")

        val action = "" + intent.action

        if (action == "android.intent.action.BOOT_COMPLETED") {

            if (SharedPreferencesManager.autoBackUp) {
                val _id = System.currentTimeMillis().toInt()
                val auto_backup_time = java.util.Calendar.getInstance(java.util.Locale.US)
                auto_backup_time[java.util.Calendar.HOUR_OF_DAY] = 1
                auto_backup_time[java.util.Calendar.MINUTE] = 0
                auto_backup_time[java.util.Calendar.SECOND] = 0
                auto_backup_time[java.util.Calendar.MILLISECOND] = 0

                SharedPreferencesManager.autoBackUpId = _id

                if (SharedPreferencesManager.autoBackUpType == 0) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 0)
                } else if (SharedPreferencesManager.autoBackUpType == 1) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 1)
                } else if (SharedPreferencesManager.autoBackUpType == 2) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 2)
                }
            }
        } else {
            val backupHelper = rpt.tool.waterdiary.utils.helpers.BackupHelper(context)
            backupHelper.createAutoBackSetup()
        }
    }

    companion object {
        private const val DEBUG_TAG = "AlarmReceiver"
    }
}