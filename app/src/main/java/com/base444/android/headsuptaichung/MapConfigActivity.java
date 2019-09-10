package com.base444.android.headsuptaichung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

public class MapConfigActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    static int REQUEST_CODE_CONFIG = 10001;
    static int RETURN_CODE_OK = 10002;

    private CheckBox isStartToday;
    private CheckBox isOnGoing;
    private CheckBox isSchedule;
    private CheckBox showAll;

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
        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();

        isStartToday.setChecked(caseMarkerFilter.isStart());
        isStartToday.setOnCheckedChangeListener(this);
        isOnGoing.setChecked(caseMarkerFilter.isOnGoing());
        isOnGoing.setOnCheckedChangeListener(this);
        isSchedule.setChecked(caseMarkerFilter.isSchedule());
        isSchedule.setOnCheckedChangeListener(this);
        showAll.setChecked(caseMarkerFilter.isShowAll());
        showAll.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        if (compoundButton.getId() == R.id.map_config_is_start){
            CaseMarkerFilter.getInstance().setStart(isCheck);
        }
        if (compoundButton.getId() == R.id.map_config_all) {
            CaseMarkerFilter.getInstance().setShowAll(isCheck);
        }
        if (compoundButton.getId() == R.id.map_config_schedule) {
            CaseMarkerFilter.getInstance().setSchedule(isCheck);
        }
        if (compoundButton.getId() == R.id.map_config_on_going) {
            CaseMarkerFilter.getInstance().setOnGoing(isCheck);
        }
    }
}
