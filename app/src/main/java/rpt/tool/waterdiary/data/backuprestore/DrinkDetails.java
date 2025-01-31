package rpt.tool.waterdiary.data.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrinkDetails
{
    @SerializedName("DrinkDateTime")
    @Expose
    private String drinkDateTime;
    @SerializedName("DrinkTime")
    @Expose
    private String drinkTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("DrinkDate")
    @Expose
    private String drinkDate;
    @SerializedName("ContainerMeasure")
    @Expose
    private String containerMeasure;
    @SerializedName("ContainerValue")
    @Expose
    private String containerValue;
    @SerializedName("ContainerValueOZ")
    @Expose
    private String containerValueOZ;

    @SerializedName("TodayGoal")
    @Expose
    private String todayGoal;
    @SerializedName("TodayGoalOZ")
    @Expose
    private String todayGoalOZ;

    public String getDrinkDateTime() {
        return drinkDateTime;
    }

    public void setDrinkDateTime(String drinkDateTime) {
        this.drinkDateTime = drinkDateTime;
    }

    public String getDrinkTime() {
        return drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrinkDate() {
        return drinkDate;
    }

    public void setDrinkDate(String drinkDate) {
        this.drinkDate = drinkDate;
    }

    public String getContainerMeasure() {
        return containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure) {
        this.containerMeasure = containerMeasure;
    }

    public String getContainerValue() {
        return containerValue;
    }

    public void setContainerValue(String containerValue) {
        this.containerValue = containerValue;
    }

    public String getContainerValueOZ() {
        return containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ) {
        this.containerValueOZ = containerValueOZ;
    }

    //===============

    public String getTodayGoal() {
        return todayGoal;
    }

    public void setTodayGoal(String todayGoal) {
        this.todayGoal = todayGoal;
    }

    public String getTodayGoalOZ() {
        return todayGoalOZ;
    }

    public void setTodayGoalOZ(String todayGoalOZ) {
        this.todayGoalOZ = todayGoalOZ;
    }
}