package com.base444.android.headsuptaichung;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.base444.android.headsuptaichung.fragments.AddLocationDialogFragment;
import com.base444.android.headsuptaichung.model.ApplicationCase;
import com.base444.android.headsuptaichung.model.CaseMarker;
import com.base444.android.headsuptaichung.model.SaveLocation;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private Button syncButton;
    private SupportMapFragment mapFragment;
    private ClusterManager<CaseMarker> mClusterManager;

    protected ProgressDialog dialog;
    protected Snackbar snackbar;
    private Polygon poly;
    private Circle circle;
    private MarkerManager.Collection normalMarkersCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        syncButton = findViewById(R.id.map_sync_item);
        frameLayout = findViewById(R.id.main_container);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);

        loadFragment(mapFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCaseDataFromGovApi();
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }


    private void showAddLocationDialog(final LatLng latLng){
        DialogFragment dialogFragment = new AddLocationDialogFragment(new AddLocationDialogFragment.MarkerSaveInterFace() {
            @Override
            public void onSaveButtonClick(String name, String note) {
                Marker melbourne = normalMarkersCollection.addMarker(new MarkerOptions().position(latLng)
                        .title(name).snippet(note)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                if (melbourne != null) {
                    SaveLocation saveLocation = new SaveLocation();
                    saveLocation.setName(melbourne.getTitle());
                    saveLocation.setNote(melbourne.getSnippet());
                    saveLocation.setLongitude(melbourne.getPosition().longitude);
                    saveLocation.setLatitude(melbourne.getPosition().latitude);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealm(saveLocation);
                    realm.commitTransaction();
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(),"");

    }

    private void getCaseDataFromGovApi() {
        showProgressDialog("Loading", "Please wait...");
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
                            mMap.clear();
                            mClusterManager.clearItems();
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

                                    CaseMarker marker = createMarker(mCase);
                                    if (marker != null){
                                        mClusterManager.addItem(marker);
                                        counter ++;
                                    }
                                }
                            }
                            realm.commitTransaction();
                            mClusterManager.cluster();
                            dismissProgressDialog();
                            Log.i("TAG", String.valueOf(counter));
                            realm.beginTransaction();
                            List<ApplicationCase> applicationCaseList = realm.where(ApplicationCase.class).findAll();
                            Log.i("TAG", String.valueOf(applicationCaseList.size()));
                            realm.commitTransaction();
                        } catch (Exception e){
                            Log.e("TAG4", e.getLocalizedMessage());
                            dismissProgressDialog();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dismissProgressDialog();
                        Log.e("TAG3", error.getErrorDetail());
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(24.1469, 120.6839);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Taiwan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        mClusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        normalMarkersCollection = mClusterManager.getMarkerManager().newCollection();

        Realm realm = Realm.getDefaultInstance();
        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();
        caseMarkerFilter.getFilterSetting(((MyApplication)getApplication()).getSettingPreferences());
        List<ApplicationCase> applicationCaseList = caseMarkerFilter.getCaseByFilter(realm);
        Log.i("TAG", String.valueOf(applicationCaseList.size()));
        loadCaseAddMarker();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showAddLocationDialog(latLng);
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (poly != null){
                    poly.remove();
                }
                if (circle != null){
                    circle.remove();
                }
            }
        });
        showMyLocation(mMap);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<CaseMarker>() {
            @Override
            public boolean onClusterItemClick(CaseMarker caseMarker) {
                drawOneArea(caseMarker.applicationCase);
                return false;
            }
        });
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<CaseMarker>() {
            @Override
            public void onClusterItemInfoWindowClick(CaseMarker caseMarker) {
                showCaseDetailInfoDialog(caseMarker.applicationCase);
            }
        });

        normalMarkersCollection.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (circle != null) {
                    circle.remove();
                }
                int radius = ((MyApplication)getApplication()).getSettingPreferences().getFilterSettingRange();
                circle = mMap.addCircle(new CircleOptions()
                    .center(marker.getPosition())
                    .radius(radius).strokeWidth(3.0f)
                    .strokeColor(Color.RED)
                    .fillColor(R.color.trans_blue));

                return false;
            }
        });
    }

    private void loadCaseAddMarker(){
        mMap.clear();
        mClusterManager.clearItems();
        Realm realm = Realm.getDefaultInstance();
        CaseMarkerFilter caseMarkerFilter = CaseMarkerFilter.getInstance();
        caseMarkerFilter.getFilterSetting(((MyApplication)getApplication()).getSettingPreferences());
        List<ApplicationCase> applicationCaseList = caseMarkerFilter.getCaseByFilter(realm);
        Log.i("TAG", String.valueOf(applicationCaseList.size()));
        for (ApplicationCase applicationCase : applicationCaseList) {
            CaseMarker marker = createMarker(applicationCase);
            if (marker != null) {
                mClusterManager.addItem(marker);
            }
        }
        mClusterManager.cluster();
        showMySavedLocation(mMap);
    }

    private void showMySavedLocation(final GoogleMap map){
        Realm realm = Realm.getDefaultInstance();
        List<SaveLocation> saveLocations = realm.where(SaveLocation.class).findAll();
        for (SaveLocation saveLocation : saveLocations) {
            normalMarkersCollection.addMarker(new MarkerOptions().position(new LatLng(saveLocation.getLatitude(), saveLocation.getLongitude()))
                    .title(saveLocation.getName()).snippet(saveLocation.getNote())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        }
    }

    private void showMyLocation(final GoogleMap map){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheckCamera == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            Nammu.askForPermission(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    , new PermissionCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void permissionGranted() {
                            map.setMyLocationEnabled(true);
                        }

                        @Override
                        public void permissionRefused() {
                            Toast.makeText(MapsActivity2.this, "Permission Refused", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void showProgressDialog(String title, String message){
        dialog = ProgressDialog.show(this,
                title, message, true);
    }
    public void dismissProgressDialog(){
        if(dialog != null) {
            if(dialog.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper)dialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if(context instanceof Activity) {
                    if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                        dialog.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    dialog.dismiss();
            }
            dialog = null;
        }
    }
    public boolean isProgressDialogShowing(){
        if(dialog != null) {
            if(dialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void drawOneArea(ApplicationCase applicationCase){
        if (poly != null){
            poly.remove();
        }
        if (applicationCase.getArea_location() != null) {
            String areaString = applicationCase.getArea_location();
            if (areaString.contains("(") && areaString.contains(")")) {
                String polyString = areaString.substring(areaString.indexOf("(") + 2, areaString.indexOf(")"));
                String[] areaPointList = polyString.split(",");
                Log.i("TAG3", polyString);
                List<LatLng> pointList = new ArrayList<>();
                for (String string : areaPointList) {
                    String tempList[] = string.split(" ");
                    pointList.add(new LatLng(Double.parseDouble(tempList[2]), Double.parseDouble(tempList[1])));
                }
                poly = mMap.addPolygon(new PolygonOptions().addAll(pointList)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
            }

        }

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    protected CaseMarker createMarker(ApplicationCase applicationCase) {

        if (applicationCase.getLocation_y() != null && !applicationCase.getLocation_y().isEmpty() &&
                applicationCase.getLocatoin_x() !=null && !applicationCase.getLocatoin_x().isEmpty()){
            try{
                LatLng position = new LatLng(Double.parseDouble(applicationCase.getLocation_y()), Double.parseDouble(applicationCase.getLocatoin_x()));
                return new CaseMarker(position, applicationCase.get工程名稱(),
                        "日期:" + applicationCase.get核准起日() + " - " + applicationCase.get核准迄日(), applicationCase);
            } catch (Exception e){
                Log.e("TAG3", e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.botton_setting:
                MapConfigActivity.startActivity(this);
                break;
            case R.id.botton_location:
                LocationListActivity.startActivity(this);
                break;
            case R.id.botton_map:
                AlarmListActivity.startActivity(this);
                break;
            default:
            break;
        }
        return false;
    }

    private void showCaseDetailInfoDialog(ApplicationCase applicationCase){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.case_detial_info_dialog, null);
        ((TextView) v.findViewById(R.id.case_info_dialog_case_type)).setText(applicationCase.get案件類別());
        ((TextView) v.findViewById(R.id.case_info_dialog_pipe_type)).setText(applicationCase.get管線工程類別());
        ((TextView) v.findViewById(R.id.case_info_dialog_start_date_type)).setText(applicationCase.get核准起日());
        ((TextView) v.findViewById(R.id.case_info_dialog_end_date_type)).setText(applicationCase.get核准迄日());
        ((TextView) v.findViewById(R.id.case_info_dialog_area)).setText(applicationCase.get區域名稱());
        ((TextView) v.findViewById(R.id.case_info_dialog_location)).setText(applicationCase.get地點());
        new AlertDialog.Builder(this)
                .setTitle(applicationCase.get工程名稱())
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MapConfigActivity.RETURN_CODE_OK){
            loadCaseAddMarker();
        }
    }
}
