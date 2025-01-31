package rpt.tool.waterdiary.data.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlarmSubDetails
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
    @SerializedName("SuperId")
    @Expose
    private String superId;

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

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }
}