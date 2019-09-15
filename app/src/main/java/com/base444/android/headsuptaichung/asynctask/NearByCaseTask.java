package com.base444.android.headsuptaichung.asynctask;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.base444.android.headsuptaichung.CaseMarkerFilter;
import com.base444.android.headsuptaichung.MyApplication;
import com.base444.android.headsuptaichung.model.ApplicationCase;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import io.realm.Realm;

public class NearByCaseTask extends AsyncTask<LatLng, Integer, String> {

    TextView messageText;

    Integer counter = 0;
    private Activity context;

    public NearByCaseTask(TextView distanceMessage, Activity context) {
        messageText = distanceMessage;
        this.context = context;
    }

    @Override
    protected String doInBackground(LatLng... latLngs) {

        Realm realm = Realm.getDefaultInstance();
        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();
        caseMarkerFilter.getFilterSetting(((MyApplication)context.getApplication()).getSettingPreferences());
        List<ApplicationCase> applicationCaseList = caseMarkerFilter.getCaseByFilter(realm);
        final int radius = ((MyApplication)context.getApplication()).getSettingPreferences().getFilterSettingRange();
        for (ApplicationCase applicationCase : applicationCaseList) {
            try {
                if (applicationCase.getLocation_y() != null && !applicationCase.getLocation_y().isEmpty() &&
                        applicationCase.getLocatoin_x() !=null && !applicationCase.getLocatoin_x().isEmpty()){
                    LatLng position = new LatLng(Double.parseDouble(applicationCase.getLocation_y()), Double.parseDouble(applicationCase.getLocatoin_x()));
                    Double distance = SphericalUtil.computeDistanceBetween(latLngs[0], position);
                    if (distance < radius){
                        counter ++;
                    }
                }
            } catch (Exception e){
                Log.e("NearByCaseTask", e.getMessage());
            }

        }
        return "附近施工數量: " + String.valueOf(counter);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (messageText != null) {
            messageText.setText(s);
        }
    }
}
