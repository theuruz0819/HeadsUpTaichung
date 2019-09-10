package com.base444.android.headsuptaichung;

import android.content.SharedPreferences;

public class SettingPreferences extends Preferences<SettingPreferences> {

    public static final String LANGUAGE_SETTING = "languageSetting";
    public static final String FILTER_SETTING_RANGE = "filterSettingRange";

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

    public Integer getLanguageSetting(){
        return sharedPreferences.getInt(LANGUAGE_SETTING, 0);
    }
    public SettingPreferences setLanguage(Integer languageSetting) throws Exception{
        ensureBeginEdit();
        mEditor.putInt(LANGUAGE_SETTING, languageSetting);
        return this;
    }
}
