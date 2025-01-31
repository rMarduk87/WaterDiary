package rpt.tool.waterdiary.utils.managers

import android.content.Context
import android.content.SharedPreferences
import rpt.tool.waterdiary.Application
import rpt.tool.waterdiary.AppUtils


object SharedPreferencesManager {
    private val ctx: Context
        get() = Application.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var autoBackUp: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.AUTO_BACK_UP, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.AUTO_BACK_UP, value).apply()
    var autoBackUpId: Int
        get() = sharedPreferences.getInt(AppUtils.AUTO_BACK_UP_ID, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.AUTO_BACK_UP_ID, value).apply()
    var autoBackUpType: Int
        get() = sharedPreferences.getInt(AppUtils.AUTO_BACK_UP_TYPE, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.AUTO_BACK_UP_TYPE, value).apply()
    var dailyWater: Float
        get() = sharedPreferences.getFloat(AppUtils.DAILY_WATER, 0f)
        set(value) = sharedPreferences.edit().putFloat(AppUtils.DAILY_WATER, value).apply()
    var isManualReminder: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_MANUAL_REMINDER, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.IS_MANUAL_REMINDER, value).apply()
    var personWeight: String
        get() = sharedPreferences.getString(AppUtils.PERSON_WEIGHT, "").toString()
        set(value) = sharedPreferences.edit().putString(AppUtils.PERSON_WEIGHT, value).apply()
    var personHeight: String?
        get() = sharedPreferences.getString(AppUtils.PERSON_HEIGHT, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.PERSON_HEIGHT, value).apply()
    var personWeightUnit: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.PERSON_WEIGHT_UNIT, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.PERSON_WEIGHT_UNIT, value).apply()
    var personHeightUnit: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.PERSON_HEIGHT_UNIT, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.PERSON_HEIGHT_UNIT, value).apply()
    var reminderOpt: Int
        get() = sharedPreferences.getInt(AppUtils.REMINDER_OPTION, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.REMINDER_OPTION, value).apply()
    var reminderSound: Int
        get() = sharedPreferences.getInt(AppUtils.REMINDER_SOUND, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.REMINDER_SOUND, value).apply()
    var disableNotification: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DISABLE_NOTIFICATION, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.DISABLE_NOTIFICATION, value).apply()
    var reminderVibrate: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.REMINDER_VIBRATE, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.REMINDER_VIBRATE, value).apply()
    var userName: String?
        get() = sharedPreferences.getString(AppUtils.USER_NAME, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.USER_NAME, value).apply()
    var userGender: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.USER_GENDER, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.USER_GENDER, value).apply()
    var disableSoundWhenAddWater: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DISABLE_SOUND_WHEN_ADD_WATER, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.DISABLE_SOUND_WHEN_ADD_WATER, value).apply()
    var waterUnit: String?
        get() = sharedPreferences.getString(AppUtils.WATER_UNIT, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.WATER_UNIT, value).apply()
    var hideWelcomeScreen: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.HIDE_WELCOME_SCREEN, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.HIDE_WELCOME_SCREEN, value).apply()
    var selectedContainer: Int
        get() = sharedPreferences.getInt(AppUtils.SELECTED_CONTAINER, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.SELECTED_CONTAINER, value).apply()
    var wakeUpTime: String?
        get() = sharedPreferences.getString(AppUtils.WAKE_UP_TIME, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.WAKE_UP_TIME, value).apply()
    var bedTime: String?
        get() = sharedPreferences.getString(AppUtils.BED_TIME, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.BED_TIME, value).apply()
    var ignoreNextStep: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IGNORE_NEXT_STEP, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.IGNORE_NEXT_STEP, value).apply()
    var interval: Int
        get() = sharedPreferences.getInt(AppUtils.INTERVAL, 30)
        set(value) = sharedPreferences.edit().putInt(AppUtils.INTERVAL, value).apply()
    var wakeUpTimeHour: Int
        get() = sharedPreferences.getInt(AppUtils.WAKE_UP_TIME_HOUR, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WAKE_UP_TIME_HOUR, value).apply()
    var wakeUpTimeMinute: Int
        get() = sharedPreferences.getInt(AppUtils.WAKE_UP_TIME_MINUTE, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WAKE_UP_TIME_MINUTE,value).apply()
    var bedTimeHour: Int
        get() = sharedPreferences.getInt(AppUtils.BED_TIME_HOUR, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.BED_TIME_HOUR, value).apply()
    var bedTimeMinute: Int
        get() = sharedPreferences.getInt(AppUtils.BED_TIME_MINUTE, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.BED_TIME_MINUTE, value).apply()
    var weatherConditions: Int
        get() = sharedPreferences.getInt(AppUtils.WEATHER_CONSITIONS, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WEATHER_CONSITIONS, value).apply()
    var setManuallyGoal: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_MANUALLY_GOAL, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_MANUALLY_GOAL, value).apply()
    var setManuallyGoalValue: Float
        get() = sharedPreferences.getFloat(AppUtils.SET_MANUALLY_GOAL_VALUE, 0f)
        set(value) = sharedPreferences.edit().putFloat(AppUtils.SET_MANUALLY_GOAL_VALUE, value).apply()
    var isActive: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_ACTIVE, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.IS_ACTIVE, value).apply()
    var isPregnant: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_PREGNANT, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.IS_PREGNANT, value).apply()
    var isBreastfeeding: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_BREASTFEEDING, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.IS_BREASTFEEDING, value).apply()
    var userPhoto: String?
        get() = sharedPreferences.getString(AppUtils.USER_PHOTO, "")
        set(value) = sharedPreferences.edit().putString(AppUtils.USER_PHOTO, value).apply()
    var menu: Int
        get() = sharedPreferences.getInt(AppUtils.MENU, 1)
        set(value) = sharedPreferences.edit().putInt(AppUtils.MENU, value).apply()
}