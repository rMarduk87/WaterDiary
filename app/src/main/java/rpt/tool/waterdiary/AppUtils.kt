package rpt.tool.waterdiary

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.sqlite.SQLiteDatabase
import android.media.Ringtone
import java.text.DecimalFormat


class AppUtils {
    companion object {


        const val USERS_SHARED_PREF : String = "user_pref"

        const val PRIVACY_POLICY_ULR: String = "https://privacy-policy.html"
        const val APP_SHARE_URL: String = "https://share.html"
        const val DATE_FORMAT: String = "dd-MM-yyyy"
        var DAILY_WATER_VALUE: Float = 0f
        var WATER_UNIT_VALUE: String = "ML"
        const val DAILY_WATER: String = "daily_water"
        const val WATER_UNIT: String = "water_unit"
        const val SELECTED_CONTAINER: String = "selected_container"
        const val HIDE_WELCOME_SCREEN: String = "hide_welcome_screen"
        const val USER_NAME: String = "user_name"
        const val USER_GENDER: String = "user_gender"
        const val USER_PHOTO: String = "user_photo"
        const val PERSON_HEIGHT: String = "person_height"
        const val PERSON_HEIGHT_UNIT: String = "person_height_unit"
        const val PERSON_WEIGHT: String = "person_weight"
        const val PERSON_WEIGHT_UNIT: String = "person_weight_unit"
        const val SET_MANUALLY_GOAL: String = "set_manually_goal"
        const val SET_MANUALLY_GOAL_VALUE: String = "set_manually_goal_value"
        const val WAKE_UP_TIME: String = "wakeup_time"
        const val WAKE_UP_TIME_HOUR: String = "wakeup_time_hour"
        const val WAKE_UP_TIME_MINUTE: String = "wakeup_time_minute"
        const val BED_TIME: String = "bed_time"
        const val BED_TIME_HOUR: String = "bed_time_hour"
        const val BED_TIME_MINUTE: String = "bed_time_minute"
        const val INTERVAL: String = "interval"
        const val REMINDER_OPTION: String = "reminder_option" // o for auto, 1 for off, 2 for silent
        const val REMINDER_VIBRATE: String = "reminder_vibrate"
        const val REMINDER_SOUND: String = "reminder_sound"
        const val DISABLE_NOTIFICATION: String = "disable_notification"
        const val IS_MANUAL_REMINDER: String = "manual_reminder_active"
        const val IS_BLOOD_DONOR: String = "blood_donor"
        const val DISABLE_SOUND_WHEN_ADD_WATER: String = "disable_sound_when_add_water"
        const val IGNORE_NEXT_STEP: String = "ignore_next_step"
        val decimalFormat: DecimalFormat = DecimalFormat("#0.00")
        val decimalFormat2: DecimalFormat = DecimalFormat("#0.0")
        var notification_ringtone: Ringtone? = null
        var RELOAD_DASHBOARD: Boolean = true
        const val LOAD_VIDEO_ADS: Boolean = false
        const val APP_DIRECTORY_NAME: String = "Water Diary"
        const val APP_PROFILE_DIRECTORY_NAME: String = "profile"
        const val AUTO_BACK_UP: String = "auto_backup"
        const val AUTO_BACK_UP_TYPE: String = "auto_backup_type"
        const val AUTO_BACK_UP_ID: String = "auto_backup_id"
        const val IS_ACTIVE: String = "is_active"
        const val IS_PREGNANT: String = "is_pregnant"
        const val IS_BREASTFEEDING: String = "is_breastfeeding"
        const val WEATHER_CONSITIONS: String = "weather_conditions"
        const val MENU: String = "menu"
        const val MALE_WATER: Double = 35.71
        const val ACTIVE_MALE_WATER: Double = 50.0
        const val DEACTIVE_MALE_WATER: Double = 14.29
        const val FEMALE_WATER: Double = 28.57
        const val ACTIVE_FEMALE_WATER: Double = 40.0
        const val DEACTIVE_FEMALE_WATER: Double = 11.43
        const val PREGNANT_WATER: Double = 700.0
        const val BREASTFEEDING_WATER: Double = 700.0
        const val WEATHER_SUNNY: Double = 1.0
        const val WEATHER_CLOUDY: Double = 0.85
        const val WEATHER_RAINY: Double = 0.68
        const val WEATHER_SNOW: Double = 0.88


        var SDB: SQLiteDatabase? = null
        const val DATABASE_NAME: String = "rptWater.db"
        const val DEVELOPER_MODE: Boolean = true
        var share_purchase_title: String = "Share To"
        var launchables: MutableList<ResolveInfo>? = null
        var pm: PackageManager? = null
        var launchables_sel: MutableList<ResolveInfo>? = null
        const val general_share_title: String = "Share"
        const val PICK_CONTACT: Int = 1000
        const val no_internet_message: String = "No Internet Connection!!!"
        const val youTubeUrlRegEx: String =
            "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
        val videoIdRegex: Array<String> = arrayOf(
            "\\?vi?=([^&]*)",
            "watch\\?.*v=([^&]*)",
            "(?:embed|vi?)/([^/?]*)",
            "^([A-Za-z0-9\\-]*)"
        )


    }
}