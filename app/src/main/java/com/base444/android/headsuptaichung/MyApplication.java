package com.base444.android.headsuptaichung;


import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

public class MyApplication extends MultiDexApplication {

    private SharedPreferences mSettingPreferences;
    
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        Realm.init(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
    public SettingPreferences getSettingPreferences() {
        return new SettingPreferences(mSettingPreferences);
    }
}
