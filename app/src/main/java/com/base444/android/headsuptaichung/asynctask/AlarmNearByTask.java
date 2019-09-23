package com.base444.android.headsuptaichung.asynctask;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.base444.android.headsuptaichung.AlarmUtil;
import com.base444.android.headsuptaichung.CaseMarkerFilter;
import com.base444.android.headsuptaichung.SettingPreferences;
import com.base444.android.headsuptaichung.model.ApplicationCase;
import com.base444.android.headsuptaichung.model.SaveLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import io.realm.Realm;

public class AlarmNearByTask extends AsyncTask {
    private Context context;
    private String message;
    public AlarmNearByTask(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        SettingPreferences settingPreferences =
                new SettingPreferences(context.getSharedPreferences("Setting", Application.MODE_PRIVATE));

        message = "Your Saved location has no construction near by";
        Realm realm = Realm.getDefaultInstance();
        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();
        caseMarkerFilter.getFilterSetting(settingPreferences);
        List<ApplicationCase> applicationCaseList = caseMarkerFilter.getCaseByFilter(realm);
        List<SaveLocation> saveLocations = realm.where(SaveLocation.class).findAll();
        final int radius = (settingPreferences.getFilterSettingRange());
        int alarmLocationCounter = 0;
        for (ApplicationCase applicationCase : applicationCaseList) {
            try {
                if (applicationCase.getLocation_y() != null && !applicationCase.getLocation_y().isEmpty() &&
                        applicationCase.getLocatoin_x() !=null && !applicationCase.getLocatoin_x().isEmpty()){
                    for (SaveLocation saveLocation : saveLocations) {
                        LatLng position = new LatLng(Double.parseDouble(applicationCase.getLocation_y()), Double.parseDouble(applicationCase.getLocatoin_x()));
                        LatLng savePosition = new LatLng(saveLocation.getLatitude(), saveLocation.getLongitude());
                        Double distance = SphericalUtil.computeDistanceBetween(savePosition, position);
                        if (distance < radius){
                            alarmLocationCounter ++;
                            break;
                        }
                    }
                }

            } catch (Exception e){
                Log.e("NearByCaseTask", e.getMessage());
            }
        }
        if (alarmLocationCounter > 0){
            message = "Your have " + String.valueOf(alarmLocationCounter) + "Saved location has  construction near by";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        AlarmUtil.showNotification(context, message);
    }
}
