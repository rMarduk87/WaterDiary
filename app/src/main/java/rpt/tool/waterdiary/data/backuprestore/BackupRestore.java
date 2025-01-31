package rpt.tool.waterdiary.data.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BackupRestore
{
    @SerializedName("AlarmDetails")
    @Expose
    private List<AlarmDetails> alarmDetailsList=new ArrayList<>();

    @SerializedName("ContainerDetails")
    @Expose
    private List<ContainerDetails> containerDetailsList=new ArrayList<>();

    @SerializedName("DrinkDetails")
    @Expose
    private List<DrinkDetails> drinkDetailsList=new ArrayList<>();

    public List<AlarmDetails> getAlarmDetails() {
        return alarmDetailsList;
    }

    public void setAlarmDetails(List<AlarmDetails> alarmDetailsList) {
        this.alarmDetailsList = alarmDetailsList;
    }

    public List<ContainerDetails> getContainerDetails() {
        return containerDetailsList;
    }

    public void setContainerDetails(List<ContainerDetails> containerDetailsList) {
        this.containerDetailsList = containerDetailsList;
    }

    public List<DrinkDetails> getDrinkDetails() {
        return drinkDetailsList;
    }

    public void setDrinkDetails(List<DrinkDetails> drinkDetailsList) {
        this.drinkDetailsList = drinkDetailsList;
    }

    //=================================

    @SerializedName("total_weight")
    @Expose
    private String total_weight;

    @SerializedName("total_drink")
    @Expose
    private float total_drink;

    @SerializedName("isKgUnit")
    @Expose
    private boolean isKgUnit=true;

    @SerializedName("isMlUnit")
    @Expose
    private boolean isMlUnit=true;

    public String getTotalWeight() {
        return total_weight;
    }

    public void setTotalWeight(String total_weight) {
        this.total_weight = total_weight;
    }

    public float getTotalDrink() {
        return total_drink;
    }

    public void setTotalDrink(float total_drink) {
        this.total_drink = total_drink;
    }

    public boolean isKgUnit() {
        return isKgUnit;
    }

    public void isKgUnit(boolean isKgUnit) {
        this.isKgUnit = isKgUnit;
    }

    public boolean isMlUnit() {
        return isMlUnit;
    }

    public void isMlUnit(boolean isMlUnit) {
        this.isMlUnit = isMlUnit;
    }

    //=========

    @SerializedName("total_height")
    @Expose
    private String total_height;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_gender")
    @Expose
    private boolean user_gender=true;

    @SerializedName("isCMUnit")
    @Expose
    private boolean isCMUnit=true;

    public String getTotalHeight() {
        return total_height;
    }

    public void setTotalHeight(String total_height) {
        this.total_height = total_height;
    }



    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public boolean getUserGender() {
        return user_gender;
    }

    public void setUserGender(boolean user_gender) {
        this.user_gender = user_gender;
    }

    public boolean isCMUnit() {
        return isCMUnit;
    }

    public void isCMUnit(boolean isCMUnit) {
        this.isCMUnit = isCMUnit;
    }

    //===========================

    @SerializedName("reminder_option")
    @Expose
    private Integer reminder_option;

    @SerializedName("reminder_vibrate")
    @Expose
    private boolean reminder_vibrate=true;

    @SerializedName("reminder_sound")
    @Expose
    private Integer reminder_sound;

    @SerializedName("disable_notification")
    @Expose
    private boolean disable_notification=false;

    @SerializedName("manual_reminder_active")
    @Expose
    private boolean manual_reminder_active=true;


    public Integer getReminderOption() {
        return reminder_option;
    }

    public void setReminderOption(Integer reminder_option) {
        this.reminder_option = reminder_option;
    }

    public Integer getReminderSound() {
        return reminder_sound;
    }

    public void setReminderSound(Integer reminder_sound) {
        this.reminder_sound = reminder_sound;
    }


    public boolean isReminderVibrate() {
        return reminder_vibrate;
    }

    public void isReminderVibrate(boolean reminder_vibrate) {
        this.reminder_vibrate = reminder_vibrate;
    }

    public boolean isDisableNotifiction() {
        return disable_notification;
    }

    public void isDisableNotifiction(boolean disable_notification) {
        this.disable_notification = disable_notification;
    }

    public boolean isManualReminderActive() {
        return manual_reminder_active;
    }

    public void isManualReminderActive(boolean manual_reminder_active) {
        this.manual_reminder_active = manual_reminder_active;
    }


    //================

    @SerializedName("disable_sound")
    @Expose
    private boolean disable_sound=false;

    public boolean isDisableSound() {
        return disable_sound;
    }

    public void isDisableSound(boolean disable_sound) {
        this.disable_sound = disable_sound;
    }


    //================================

    @SerializedName("auto_backup")
    @Expose
    private boolean auto_backup=false;

    public boolean isAutoBackup() {
        return auto_backup;
    }

    public void isAutoBackup(boolean auto_backup) {
        this.auto_backup = auto_backup;
    }

    @SerializedName("auto_backup_type")
    @Expose
    private Integer auto_backup_type=0;

    public Integer getAutoBackupType() {
        return auto_backup_type;
    }

    public void setAutoBackupType(Integer auto_backup_type) {
        this.auto_backup_type = auto_backup_type;
    }


    @SerializedName("auto_backup_id")
    @Expose
    private Integer auto_backup_id=0;

    public Integer getAutoBackupId() {
        return auto_backup_id;
    }

    public void setAutoBackupID(Integer auto_backup_id) {
        this.auto_backup_id = auto_backup_id;
    }


    //================================


    @SerializedName("is_active")
    @Expose
    private boolean is_active=false;

    public boolean isActive() {
        return is_active;
    }

    public void isActive(boolean is_active) {
        this.is_active = is_active;
    }


    @SerializedName("is_pregnant")
    @Expose
    private boolean is_pregnant=false;

    public boolean isPregnant() {
        return is_pregnant;
    }

    public void isPregnant(boolean is_pregnant) {
        this.is_pregnant = is_pregnant;
    }


    @SerializedName("is_breastfeeding")
    @Expose
    private boolean is_breastfeeding=false;

    public boolean isBreastfeeding() {
        return is_breastfeeding;
    }

    public void isBreastfeeding(boolean is_breastfeeding) {
        this.is_breastfeeding = is_breastfeeding;
    }


    @SerializedName("weather_conditions")
    @Expose
    private Integer weather_conditions=0;

    public Integer getWeatherConditions() {
        return weather_conditions;
    }

    public void setWeatherConditions(Integer weather_conditions) {
        this.weather_conditions = weather_conditions;
    }
}