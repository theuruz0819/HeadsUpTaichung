package com.base444.android.headsuptaichung;


import android.app.Application;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

public class MyApplication extends Application {

    private SharedPreferences mSettingPreferences;
    
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext(), HttpUtil.getUnsafeOkHttpClient());
        Realm.init(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
        mSettingPreferences = getSharedPreferences("Setting", MODE_PRIVATE);

    }
    public SettingPreferences getSettingPreferences() {
        return new SettingPreferences(mSettingPreferences);
    }
}
