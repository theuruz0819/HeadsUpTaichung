package com.base444.android.headsuptaichung.asynctask;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.base444.android.headsuptaichung.MyApplication;
import com.base444.android.headsuptaichung.SettingPreferences;

public class CaseUpdateScheduleJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        SettingPreferences settingPreferences = ((MyApplication)getApplication()).getSettingPreferences();
        new UpdateDbTask(settingPreferences).execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
