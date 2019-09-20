package com.base444.android.headsuptaichung;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.adapter.AlarmListAdapter;
import com.base444.android.headsuptaichung.model.AlarmItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import io.realm.Realm;

public class AlarmListActivity extends AppCompatActivity {

    static int REQUEST_CODE_CONFIG = 10001;
    static int RETURN_CODE_OK = 10002;

    private RecyclerView recyclerView;
    private AlarmListAdapter alarmListAdapter;

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, AlarmListActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE_CONFIG);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        recyclerView = findViewById(R.id.alarm_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmListAdapter = new AlarmListAdapter();
        recyclerView.setAdapter(alarmListAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(AlarmListActivity.this, new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AlarmItem alarmItem = new AlarmItem();
                        alarmItem.setHour(hourOfDay);
                        alarmItem.setMinute(minute);
                        alarmItem.setEnable(true);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.copyToRealm(alarmItem);
                        realm.commitTransaction();
                        alarmListAdapter.dataUpdate();
                        alarmListAdapter.notifyDataSetChanged();
                        Toast.makeText(AlarmListActivity.this, "現在時間是" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, false).show();
            }
        });
    }
}
