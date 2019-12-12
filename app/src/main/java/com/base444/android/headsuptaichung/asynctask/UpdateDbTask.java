package com.base444.android.headsuptaichung.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.base444.android.headsuptaichung.SettingPreferences;
import com.base444.android.headsuptaichung.model.ApplicationCase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class UpdateDbTask extends AsyncTask<String, Integer, String> {
    private SettingPreferences settingPreferences;

    public UpdateDbTask(SettingPreferences settingPreferences) {
        this.settingPreferences = settingPreferences;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.i("TAG3", "UpdateDbTask  doInBackground");
        AndroidNetworking.get("http://datacenter.taichung.gov.tw/swagger/OpenData/b77b2146-9e3f-4e5f-a31b-cef171c0285b")
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
                                Log.i("TAG3", temp3);
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
                            realm.beginTransaction();
                            List<ApplicationCase> applicationCaseList = realm.where(ApplicationCase.class).findAll();
                            Log.i("TAG", String.valueOf(applicationCaseList.size()));
                            realm.commitTransaction();

                            settingPreferences.beginEdit();
                            settingPreferences.setLastUpdateTime(new Date().getTime());
                            settingPreferences.commit();
                            Log.i("TAG3", "UpdateDbTask  onPostExecute");

                        } catch (Exception e){
                            Log.e("TAG4", e.getLocalizedMessage());

                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("TAG3", error.getErrorDetail());
                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
