package com.base444.android.headsuptaichung.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlarmItem extends RealmObject {
    @PrimaryKey
    private Long createTimePk;
    private Integer hour;
    private Integer minute;
    private Boolean isEnable;
    private RealmList<String> enableDays;

    public AlarmItem() {
        this.createTimePk = new Date().getTime();
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public int getRequestId(){
        return hour * 100 + minute;
    }

    public RealmList<String> getEnableDays() {
        return enableDays;
    }

    public void setEnableDays(RealmList<String> enableDays) {
        this.enableDays = enableDays;
    }
}
