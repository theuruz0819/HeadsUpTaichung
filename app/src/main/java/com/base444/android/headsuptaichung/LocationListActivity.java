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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

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

        AdView mAdView =  findViewById(R.id.location_list_adview);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
