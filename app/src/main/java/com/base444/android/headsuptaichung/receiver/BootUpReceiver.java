package com.base444.android.headsuptaichung.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.base444.android.headsuptaichung.AlarmUtil;
import com.base444.android.headsuptaichung.model.AlarmItem;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Realm realm = Realm.getDefaultInstance();
            realm.where(AlarmItem.class).findAll();
            List<AlarmItem> alarmItems = realm.where(AlarmItem.class).findAll();
            for (AlarmItem alarmItem : alarmItems) {
                if (alarmItem.getEnable()){
                    Calendar calendar = Calendar.getInstance();
                    if (calendar.get(Calendar.HOUR_OF_DAY) > alarmItem.getHour()){
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, alarmItem.getHour());
                    calendar.set(Calendar.MINUTE, alarmItem.getMinute());
                    AlarmUtil.add_alarm(context, calendar, alarmItem.getRequestId(), true);
                }
            }
        }
    }
}