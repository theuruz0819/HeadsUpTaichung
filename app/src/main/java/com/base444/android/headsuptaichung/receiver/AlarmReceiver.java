package com.base444.android.headsuptaichung.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.base444.android.headsuptaichung.asynctask.AlarmNearByTask;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        Log.i("TAG3", "onReceive");
        if(bData.get("title").equals("activity_app"))
        {
            new AlarmNearByTask(context).execute();
        }
    }

}
