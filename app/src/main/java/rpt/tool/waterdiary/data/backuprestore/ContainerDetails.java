package rpt.tool.waterdiary.data.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContainerDetails
{
    @SerializedName("ContainerID")
    @Expose
    private String containerID;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ContainerMeasure")
    @Expose
    private String containerMeasure;
    @SerializedName("ContainerValue")
    @Expose
    private String containerValue;
    @SerializedName("ContainerValueOZ")
    @Expose
    private String containerValueOZ;
    @SerializedName("IsOpen")
    @Expose
    private String isOpen;
    @SerializedName("IsCustom")
    @Expose
    private String isCustom;


    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(String isCustom) {
        this.isCustom = isCustom;
    }
}