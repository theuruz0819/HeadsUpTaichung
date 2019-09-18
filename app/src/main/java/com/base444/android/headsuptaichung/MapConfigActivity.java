package com.base444.android.headsuptaichung;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.base444.android.headsuptaichung.asynctask.CaseUpdateScheduleJobService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MapConfigActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    static int REQUEST_CODE_CONFIG = 10001;
    static int RETURN_CODE_OK = 10002;

    private CheckBox isStartToday;
    private CheckBox isOnGoing;
    private CheckBox isSchedule;
    private CheckBox showAll;
    private TextView nearByRangeInfoText;
    private ToggleButton autoUpdateToggle;
    private TextView lastUpdateDate;

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, MapConfigActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE_CONFIG);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_config);
        setResult(RETURN_CODE_OK,null);
        isStartToday = findViewById(R.id.map_config_is_start);
        isOnGoing = findViewById(R.id.map_config_on_going);
        isSchedule = findViewById(R.id.map_config_schedule);
        showAll = findViewById(R.id.map_config_all);
        autoUpdateToggle = findViewById(R.id.map_config_auto_sync_toggle);
        lastUpdateDate = findViewById(R.id.map_config_last_update_date);

        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();
        caseMarkerFilter.getFilterSetting(((MyApplication)getApplication()).getSettingPreferences());
        isStartToday.setChecked(caseMarkerFilter.isStart());
        isStartToday.setOnCheckedChangeListener(this);
        isOnGoing.setChecked(caseMarkerFilter.isOnGoing());
        isOnGoing.setOnCheckedChangeListener(this);
        isSchedule.setChecked(caseMarkerFilter.isSchedule());
        isSchedule.setOnCheckedChangeListener(this);
        showAll.setChecked(caseMarkerFilter.isShowAll());
        showAll.setOnCheckedChangeListener(this);
        autoUpdateToggle.setChecked(((MyApplication)getApplication()).getSettingPreferences().getAutoUpdateEnable());

        final List<String> rangeArray =
                Arrays.asList(MapConfigActivity.this.getResources().getStringArray(R.array.radius_range));
        final SettingPreferences setting = ((MyApplication) getApplication()).getSettingPreferences();
        final Integer currentSettingRange = setting.getFilterSettingRange();
        int targetIndex = rangeArray.indexOf(String.valueOf(currentSettingRange));

        Spinner spinner = findViewById(R.id.map_config_range_list_spinner);
        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(MapConfigActivity.this,
                R.array.radius_range,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(lunchList);
        spinner.setSelection(targetIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentSettingRange != Integer.parseInt(rangeArray.get(position))){
                    setting.beginEdit();
                    try {
                        setting.setFilterSettingRange(Integer.parseInt(rangeArray.get(position)));
                    } catch (Exception e) {
                        Log.i("MapConfigActivity", e.getMessage());
                    }
                    setting.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        autoUpdateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                SettingPreferences settingPreferences = ((MyApplication)getApplication()).getSettingPreferences();
                settingPreferences.beginEdit();
                try {
                    settingPreferences.setAutoUpdateEnable(isCheck);
                    JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
                            .getSystemService(JOB_SCHEDULER_SERVICE);

                    if (isCheck) {
                        ComponentName componentName = new ComponentName(MapConfigActivity.this,
                                CaseUpdateScheduleJobService.class);

                        JobInfo jobInfo = new JobInfo.Builder(CaseUpdateScheduleJobService.ID, componentName)
                                .setPeriodic(AlarmManager.INTERVAL_HALF_DAY).setRequiredNetworkType(
                                        JobInfo.NETWORK_TYPE_NOT_ROAMING)
                                .setPersisted(true).build();
                        jobScheduler.schedule(jobInfo);
                    } else {
                        jobScheduler.cancel(CaseUpdateScheduleJobService.ID);
                    }
                    settingPreferences.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        SettingPreferences settingPreferences = ((MyApplication)getApplication()).getSettingPreferences();
        Long lastUpdate = settingPreferences.getLastUpdateTime();
        if (lastUpdate != 0){
            lastUpdateDate.setText("最後更新 : " + new Date(lastUpdate).toString());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        try {
            SettingPreferences settingPreferences = ((MyApplication)getApplication()).getSettingPreferences();
            settingPreferences.beginEdit();
            if (compoundButton.getId() == R.id.map_config_is_start){
                CaseMarkerFilter.getInstance().setStart(isCheck);
                settingPreferences.setFilterSettingIsStart(isCheck);
            }
            if (compoundButton.getId() == R.id.map_config_all) {
                CaseMarkerFilter.getInstance().setShowAll(isCheck);
                settingPreferences.setFilterSettingShowAll(isCheck);
            }
            if (compoundButton.getId() == R.id.map_config_schedule) {
                CaseMarkerFilter.getInstance().setSchedule(isCheck);
                settingPreferences.setFilterSettingIsSchedule(isCheck);
            }
            if (compoundButton.getId() == R.id.map_config_on_going) {
                CaseMarkerFilter.getInstance().setOnGoing(isCheck);
                settingPreferences.setFilterSettingIsOngoing(isCheck);
            }
            settingPreferences.commit();
        } catch (Exception e){
            Log.i("MapConfigActivity", e.getMessage());
            Toast.makeText(this, "Setting Error", Toast.LENGTH_SHORT).show();
        }

    }
}
