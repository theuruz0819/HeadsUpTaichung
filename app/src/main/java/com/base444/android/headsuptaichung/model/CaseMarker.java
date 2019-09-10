package com.base444.android.headsuptaichung.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CaseMarker implements ClusterItem {

    public LatLng position;
    public String title;
    public String snippet;
    public ApplicationCase applicationCase;
    public CaseMarker(LatLng position, String 工程名稱, String 廠商名稱, ApplicationCase applicationCase) {
        this.position = position;
        this.title = 工程名稱;
        this.snippet = 廠商名稱;
        this.applicationCase = applicationCase;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
