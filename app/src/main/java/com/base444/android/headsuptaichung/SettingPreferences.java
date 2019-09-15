package com.base444.android.headsuptaichung;

import android.content.SharedPreferences;

public class SettingPreferences extends Preferences<SettingPreferences> {

    public static final String LANGUAGE_SETTING = "languageSetting";
    public static final String FILTER_SETTING_RANGE = "filterSettingRange";
    public static final String FILTER_SETTING_IS_START = "isStartFilter";
    public static final String FILTER_SETTING_IS_ON_GOING = "isOnGoingFilter";
    public static final String FILTER_SETTING_IS_SCHEDULE = "isScheduleFilter";
    public static final String FILTER_SETTING_SHOW_ALL = "showAllFilter";

    SettingPreferences(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    public Integer getFilterSettingRange(){
        return sharedPreferences.getInt(FILTER_SETTING_RANGE, 100);
    }

    public SettingPreferences setFilterSettingRange(Integer range) throws Exception{
        ensureBeginEdit();
        mEditor.putInt(FILTER_SETTING_RANGE, range);
        return this;
    }

    public Boolean getFilterSettingIsStart(){
        return sharedPreferences.getBoolean(FILTER_SETTING_IS_START, false);
    }

    public SettingPreferences setFilterSettingIsStart(Boolean isStart) throws Exception{
        ensureBeginEdit();
        mEditor.putBoolean(FILTER_SETTING_IS_START, isStart);
        return this;
    }
    public Boolean getFilterSettingIsOngoing(){
        return sharedPreferences.getBoolean(FILTER_SETTING_IS_ON_GOING, false);
    }

    public SettingPreferences setFilterSettingIsOngoing(boolean isOngoing) throws Exception{
        mEditor.putBoolean(FILTER_SETTING_IS_ON_GOING, isOngoing);
        return this;
    }
    public Boolean getFilterSettingIsSchedule(){
        return sharedPreferences.getBoolean(FILTER_SETTING_IS_SCHEDULE, false);
    }

    public SettingPreferences setFilterSettingIsSchedule(boolean isSchedule) throws Exception{
        ensureBeginEdit();
        mEditor.putBoolean(FILTER_SETTING_IS_SCHEDULE, isSchedule);
        return this;
    }
    public Boolean getFilterSettingShowAll(){
        return sharedPreferences.getBoolean(FILTER_SETTING_SHOW_ALL, false);
    }

    public SettingPreferences setFilterSettingShowAll(boolean showAll) throws Exception{
        ensureBeginEdit();
        mEditor.putBoolean(FILTER_SETTING_SHOW_ALL, showAll);
        return this;
    }

    public Integer getLanguageSetting(){
        return sharedPreferences.getInt(LANGUAGE_SETTING, 0);
    }

    public SettingPreferences setLanguage(Integer languageSetting) throws Exception{
        ensureBeginEdit();
        mEditor.putInt(LANGUAGE_SETTING, languageSetting);
        return this;
    }
}
