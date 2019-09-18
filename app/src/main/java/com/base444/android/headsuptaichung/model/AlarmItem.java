package com.base444.android.headsuptaichung.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AlarmItem extends RealmObject {
    private Integer hour;
    private Integer minute;
    private Boolean isEnable;
    private RealmList<String> enableDays;

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

    public RealmList<String> getEnableDays() {
        return enableDays;
    }

    public void setEnableDays(RealmList<String> enableDays) {
        this.enableDays = enableDays;
    }
}
