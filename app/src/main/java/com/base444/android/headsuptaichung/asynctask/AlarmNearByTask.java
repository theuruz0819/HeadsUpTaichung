package com.base444.android.headsuptaichung.asynctask;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.base444.android.headsuptaichung.AlarmUtil;
import com.base444.android.headsuptaichung.CaseMarkerFilter;
import com.base444.android.headsuptaichung.SettingPreferences;
import com.base444.android.headsuptaichung.model.ApplicationCase;
import com.base444.android.headsuptaichung.model.SaveLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
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

        final SettingPreferences settingPreferences =
                new SettingPreferences(context.getSharedPreferences("Setting", Application.MODE_PRIVATE));

        AndroidNetworking.get("https://datacenter.taichung.gov.tw/swagger/OpenData/b77b2146-9e3f-4e5f-a31b-cef171c0285b")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.delete(ApplicationCase.class);
                            realm.commitTransaction();

                            realm.beginTransaction();
                            int counter = 0;
                            for (int i = 0; i < response.length(); i++){
                                String temp1 = response.get(i).toString().replace("中心點X坐標", "location_x");
                                String temp2 = temp1.replace("中心點Y坐標","location_y");
                                String temp3 = temp2.replace("施工範圍坐標","area_location");
                                JSONObject jsonObject = new JSONObject(temp3);

                                ApplicationCase mCase = new Gson().fromJson(response.get(i).toString(), ApplicationCase.class);
                                if (jsonObject.get("location_x") != null) {
                                    mCase.setLocatoin_x(String.valueOf(jsonObject.get("location_x")));
                                    mCase.setLocation_y(String.valueOf(jsonObject.get("location_y")));
                                    String jsonObject12 = jsonObject.getString("area_location");
                                    mCase.setArea_location(jsonObject12);
                                    mCase.setupDate();
                                    realm.copyToRealm(mCase);

                                }
                            }
                            realm.commitTransaction();

                            settingPreferences.beginEdit();
                            settingPreferences.setLastUpdateTime(new Date().getTime());
                            settingPreferences.commit();

                            message = "Your Saved location has no construction near by";
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
                                message = "Your have " + String.valueOf(alarmLocationCounter) + " Saved location has  construction near by";
                            }
                            AlarmUtil.showNotification(context, message);
                        } catch (Exception e){
                            Log.e("TAG3", e.getLocalizedMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("TAG3", error.getErrorDetail());
                        Realm realm = Realm.getDefaultInstance();
                        message = "Your Saved location has no construction near by";
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
                            message = "Your have " + String.valueOf(alarmLocationCounter) + " Saved location has  construction near by";
                        }
                        AlarmUtil.showNotification(context, message);
                    }
                });

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
