package com.base444.android.headsuptaichung.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        if(bData.get("title").equals("activity_app"))
        {
            //主要執行的程式
        }
    }
}
