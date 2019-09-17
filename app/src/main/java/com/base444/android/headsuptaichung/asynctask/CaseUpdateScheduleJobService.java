package com.base444.android.headsuptaichung.asynctask;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.base444.android.headsuptaichung.MyApplication;
import com.base444.android.headsuptaichung.SettingPreferences;

public class CaseUpdateScheduleJobService extends JobService {
    public final static int ID = 100001;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("TAG3", "CaseUpdateScheduleJobService onStartJob");
        SettingPreferences settingPreferences = ((MyApplication)getApplication()).getSettingPreferences();
        new UpdateDbTask(settingPreferences).execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
