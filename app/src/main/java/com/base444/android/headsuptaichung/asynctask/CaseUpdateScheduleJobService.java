package com.base444.android.headsuptaichung.asynctask;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class CaseUpdateScheduleJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
