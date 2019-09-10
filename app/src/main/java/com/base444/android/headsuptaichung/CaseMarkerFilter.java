package com.base444.android.headsuptaichung;

import com.base444.android.headsuptaichung.model.ApplicationCase;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class CaseMarkerFilter {
    private static final CaseMarkerFilter ourInstance = new CaseMarkerFilter();

    public static CaseMarkerFilter getInstance() {
        return ourInstance;
    }

    private boolean isStart = true;
    private boolean isOnGoing;
    private boolean isSchedule;
    private boolean showAll;
    private boolean isSaveLocation;
    private String regineName;
    private String type;

    public List<ApplicationCase> getCaseByFilter(Realm realm){
        RealmQuery<ApplicationCase> result;
        if (isStart) {
            result = realm.where(ApplicationCase.class).greaterThanOrEqualTo("dueDate", new Date()).and().equalTo("是否開工", "是");
        } else{
            result = realm.where(ApplicationCase.class).greaterThanOrEqualTo("dueDate", new Date());
        }
        if (isOnGoing) {
            result = result.or().lessThanOrEqualTo("startDate", new Date());
        }
        if (isSchedule){
            result = result.or().greaterThan("startDate", new Date());
        }
        if (showAll){
            result = realm.where(ApplicationCase.class);
        }
        return result.findAll();
    }


    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isSaveLocation() {
        return isSaveLocation;
    }

    public void setSaveLocation(boolean saveLocation) {
        isSaveLocation = saveLocation;
    }

    public String getRegineName() {
        return regineName;
    }

    public void setRegineName(String regineName) {
        this.regineName = regineName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOnGoing() {
        return isOnGoing;
    }

    public void setOnGoing(boolean onGoing) {
        isOnGoing = onGoing;
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public void setSchedule(boolean schedule) {
        isSchedule = schedule;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }
}
