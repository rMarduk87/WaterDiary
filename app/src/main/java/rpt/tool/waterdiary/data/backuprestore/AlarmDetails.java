package rpt.tool.waterdiary.data.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AlarmDetails
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("AlarmTime")
    @Expose
    private String alarmTime;
    @SerializedName("AlarmId")
    @Expose
    private String alarmId;
    @SerializedName("AlarmType")
    @Expose
    private String alarmType;
    @SerializedName("AlarmInterval")
    @Expose
    private String alarmInterval;
    @SerializedName("AlarmSubDetails")
    @Expose
    private List<AlarmSubDetails> alarmSubDetailsList=new ArrayList<>();

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(String alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public List<AlarmSubDetails> getAlarmSubDetails() {
        return alarmSubDetailsList;
    }

    public void setAlarmSubDetails(List<AlarmSubDetails> alarmSubDetailsList) {
        this.alarmSubDetailsList = alarmSubDetailsList;
    }

    //===============

    @SerializedName("SundayAlarmId")
    @Expose
    private String alarmSundayId;
    @SerializedName("MondayAlarmId")
    @Expose
    private String alarmMondayId;
    @SerializedName("TuesdayAlarmId")
    @Expose
    private String alarmTuesdayId;
    @SerializedName("WednesdayAlarmId")
    @Expose
    private String alarmWednesdayId;
    @SerializedName("ThursdayAlarmId")
    @Expose
    private String alarmThursdayId;
    @SerializedName("FridayAlarmId")
    @Expose
    private String alarmFridayId;
    @SerializedName("SaturdayAlarmId")
    @Expose
    private String alarmSaturdayId;

    public String getAlarmSundayId() {
        return alarmSundayId;
    }

    public void setAlarmSundayId(String alarmSundayId) {
        this.alarmSundayId = alarmSundayId;
    }


    public String getAlarmMondayId() {
        return alarmMondayId;
    }

    public void setAlarmMondayId(String alarmMondayId) {
        this.alarmMondayId = alarmMondayId;
    }


    public String getAlarmTuesdayId() {
        return alarmTuesdayId;
    }

    public void setAlarmTuesdayId(String alarmTuesdayId) {
        this.alarmTuesdayId = alarmTuesdayId;
    }


    public String getAlarmWednesdayId() {
        return alarmWednesdayId;
    }

    public void setAlarmWednesdayId(String alarmWednesdayId) {
        this.alarmWednesdayId = alarmWednesdayId;
    }


    public String getAlarmThursdayId() {
        return alarmThursdayId;
    }

    public void setAlarmThursdayId(String alarmThursdayId) {
        this.alarmThursdayId = alarmThursdayId;
    }


    public String getAlarmFridayId() {
        return alarmFridayId;
    }

    public void setAlarmFridayId(String alarmFridayId) {
        this.alarmFridayId = alarmFridayId;
    }


    public String getAlarmSaturdayId() {
        return alarmSaturdayId;
    }

    public void setAlarmSaturdayId(String alarmSaturdayId) {
        this.alarmSaturdayId = alarmSaturdayId;
    }

    @SerializedName("IsOff")
    @Expose
    private Integer isOff=0;
    @SerializedName("Sunday")
    @Expose
    private Integer sunday=0;
    @SerializedName("Monday")
    @Expose
    private Integer monday=0;
    @SerializedName("Tuesday")
    @Expose
    private Integer tuesday=0;
    @SerializedName("Wednesday")
    @Expose
    private Integer wednesday=0;
    @SerializedName("Thursday")
    @Expose
    private Integer thursday=0;
    @SerializedName("Friday")
    @Expose
    private Integer friday=0;
    @SerializedName("Saturday")
    @Expose
    private Integer saturday=0;

    public Integer getIsOff() {
        return isOff;
    }

    public void setIsOff(Integer isOff) {
        this.isOff = isOff;
    }

    public Integer getSunday() {
        return sunday;
    }

    public void setSunday(Integer sunday) {
        this.sunday = sunday;
    }

    public Integer getMonday() {
        return monday;
    }

    public void setMonday(Integer monday) {
        this.monday = monday;
    }

    public Integer getTuesday() {
        return tuesday;
    }

    public void setTuesday(Integer tuesday) {
        this.tuesday = tuesday;
    }

    public Integer getWednesday() {
        return wednesday;
    }

    public void setWednesday(Integer wednesday) {
        this.wednesday = wednesday;
    }

    public Integer getThursday() {
        return thursday;
    }

    public void setThursday(Integer thursday) {
        this.thursday = thursday;
    }

    public Integer getFriday() {
        return friday;
    }

    public void setFriday(Integer friday) {
        this.friday = friday;
    }

    public Integer getSaturday() {
        return saturday;
    }

    public void setSaturday(Integer saturday) {
        this.saturday = saturday;
    }
}