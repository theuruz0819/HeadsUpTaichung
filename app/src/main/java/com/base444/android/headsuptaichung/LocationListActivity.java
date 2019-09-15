package com.base444.android.headsuptaichung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.adapter.SavedLocationAdapter;

public class LocationListActivity extends AppCompatActivity {
    static int RETURN_CODE_OK = 10002;
    static int REQUEST_CODE_LOCATION_LIST = 10003;
    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, LocationListActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE_LOCATION_LIST);
    }

    RecyclerView locationList;
    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        confirmBtn = findViewById(R.id.location_list_confirm_btn);
        locationList = findViewById(R.id.location_list);
        locationList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        SavedLocationAdapter adapter = new SavedLocationAdapter(this);
        locationList.setAdapter(adapter);
        setResult(RETURN_CODE_OK,null);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
