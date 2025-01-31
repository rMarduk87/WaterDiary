package rpt.tool.waterdiary.utils.helpers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.SelectBottleActivity
import rpt.tool.waterdiary.SelectSnoozeActivity
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


internal class NotificationHelper(private val mContext: Context) {
    var dth: DateHelper = DateHelper()
    

    init {
        if (AppUtils.notification_ringtone == null) AppUtils.notification_ringtone =
            RingtoneManager.getRingtone(
                mContext,
                sound
            )
    }

    fun createNotification() {
        d("createNotification", "" + SharedPreferencesManager.reminderOpt)
        d("createNotification V", "" + SharedPreferencesManager.reminderVibrate)



        if (SharedPreferencesManager.reminderOpt == 1) return

        if (reachedDailyGoal() && SharedPreferencesManager.disableNotification) return


        val intent = Intent(mContext, DashboardActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val resultPendingIntent: PendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val snoozeIntent = Intent(
            mContext,
            SelectSnoozeActivity::class.java
        )
        snoozeIntent.setAction("SNOOZE_ACTION")
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getActivity(mContext, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val addWaterIntent = Intent(
            mContext,
            SelectBottleActivity::class.java
        )
        addWaterIntent.setAction("ADD_WATER_ACTION")
        addWaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val addWaterPendingIntent: PendingIntent = PendingIntent.getActivity(
            mContext,
            0,
            addWaterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val mBuilder = NotificationCompat.Builder(mContext)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground)
        mBuilder.setLargeIcon(
            BitmapFactory.decodeResource(
                mContext.resources,
                R.mipmap.ic_launcher
            )
        )
        mBuilder.setContentTitle(mContext.resources.getString(R.string.str_drink_water))
            .setContentText("" + get_today_report())
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

        if (SharedPreferencesManager.reminderOpt == 0 && !SharedPreferencesManager.reminderVibrate) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL)
        } else if (SharedPreferencesManager.reminderOpt == 0 && SharedPreferencesManager.reminderVibrate) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        } else if (SharedPreferencesManager.reminderOpt == 2 && !SharedPreferencesManager.reminderVibrate) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        }

        mBuilder.addAction(
            R.drawable.ic_plus,
            mContext.resources.getString(R.string.str_add_water),
            addWaterPendingIntent
        )
        mBuilder.addAction(
            R.drawable.ic_notification,
            mContext.resources.getString(R.string.str_snooze),
            snoozePendingIntent
        )
        
        val mNotificationManager: NotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        mNotificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
        mNotificationManager.deleteNotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID)

        if (SharedPreferencesManager.reminderOpt == 0) {
            if (!SharedPreferencesManager.reminderVibrate) {
                val importance: Int = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel: NotificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "Reminder", importance
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.setSound(null, null)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )


                checkNotNull(mNotificationManager)
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)


                mNotificationManager.createNotificationChannel(notificationChannel)
            } else {
                val importance: Int = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel: NotificationChannel = NotificationChannel(
                    NOTIFICATION_VIBRATE_CHANNEL_ID, "Vibrate Reminder", importance
                )
                notificationChannel.enableLights(true)
                notificationChannel.setSound(null, null)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(false)
                notificationChannel.vibrationPattern = longArrayOf(0)


                checkNotNull(mNotificationManager)
                mBuilder.setChannelId(NOTIFICATION_VIBRATE_CHANNEL_ID)

                mNotificationManager.createNotificationChannel(notificationChannel)
            }

            try {
                if (!AppUtils.notification_ringtone!!.isPlaying) AppUtils.notification_ringtone!!.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            if (!SharedPreferencesManager.reminderVibrate) {
                val channel_none: NotificationChannel = NotificationChannel(
                    NOTIFICATION_SILENT_CHANNEL_ID,
                    "Silent Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel_none.setSound(null, null)
                channel_none.enableVibration(true)
                channel_none.vibrationPattern = longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )

                checkNotNull(mNotificationManager)
                mBuilder.setChannelId(NOTIFICATION_SILENT_CHANNEL_ID)
                mNotificationManager.createNotificationChannel(channel_none)
            } else {
                val channel_none: NotificationChannel = NotificationChannel(
                    NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID,
                    "Silent-Vibrate Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel_none.setSound(null, null)
                channel_none.enableVibration(false)
                channel_none.vibrationPattern = longArrayOf(0)
                checkNotNull(mNotificationManager)
                mBuilder.setChannelId(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID)
                mNotificationManager.createNotificationChannel(channel_none)
            }
        }
        checkNotNull(mNotificationManager)
        mNotificationManager.notify(0,  /* Request Code */mBuilder.build())
    }

    val sound: Uri
        get() {
            var uri = Settings.System.DEFAULT_NOTIFICATION_URI

            d("getSound", "" + SharedPreferencesManager.reminderSound)

            if (SharedPreferencesManager.reminderSound == 1) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.bell)
            else if (SharedPreferencesManager.reminderSound == 2) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.blop)
            else if (SharedPreferencesManager.reminderSound == 3) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.bong)
            else if (SharedPreferencesManager.reminderSound == 4) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.click)
            else if (SharedPreferencesManager.reminderSound == 5) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.echo_droplet)
            else if (SharedPreferencesManager.reminderSound == 6) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.mario_droplet)
            else if (SharedPreferencesManager.reminderSound == 7) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.ship_bell)
            else if (SharedPreferencesManager.reminderSound == 8) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.simple_droplet)
            else if (SharedPreferencesManager.reminderSound == 9) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.tiny_droplet)

            return uri
        }

    private fun reachedDailyGoal(): Boolean {
        AppUtils.SDB = mContext.openOrCreateDatabase(
            AppUtils.DATABASE_NAME,
            Context.MODE_PRIVATE,
            null
        )

        if (SharedPreferencesManager.dailyWater == 0f) {
            AppUtils.DAILY_WATER_VALUE = 2500f
        } else {
            AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        val arr_data =
            getdata("tbl_drink_details", "DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy") + "'")

        var drink_water = 0f
        for (k in arr_data.indices) {
            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                drink_water += arr_data[k]["ContainerValue"]!!.toFloat()
            else
                    drink_water += arr_data[k]["ContainerValueOZ"]!!.toFloat()
        }

        return if (drink_water >= AppUtils.DAILY_WATER_VALUE) true
        else false
    }

    @SuppressLint("WrongConstant")
    fun get_today_report(): String {
        AppUtils.SDB = mContext.openOrCreateDatabase(
            AppUtils.DATABASE_NAME,
            SQLiteDatabase.CREATE_IF_NECESSARY,
            null
        )

        if (SharedPreferencesManager.dailyWater == 0f) {
            AppUtils.DAILY_WATER_VALUE = 2500f
        } else {
            AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        if (check_blank_data("" + SharedPreferencesManager.waterUnit.toString())) {
            AppUtils.WATER_UNIT_VALUE = "ml"
        } else {
            AppUtils.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit.toString()
        }

        val arr_data =
            getdata("tbl_drink_details", "DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy") + "'")

        var drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                arr_data[k]["ContainerValue"]!!.toFloat()
            else
                arr_data[k]["ContainerValueOZ"]!!.toFloat()
        }

        return mContext.resources.getString(R.string.str_have_u_had_any_water_yet)
    }

    fun check_blank_data(data: String?): Boolean {
        return data == "" || data!!.isEmpty() || data.isEmpty() || data == "null"
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


    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val NOTIFICATION_SILENT_CHANNEL_ID = "10002"
        private const val NOTIFICATION_VIBRATE_CHANNEL_ID = "10003"
        private const val NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID = "10004"
    }
}