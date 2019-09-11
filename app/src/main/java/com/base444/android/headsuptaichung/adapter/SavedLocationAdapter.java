package com.base444.android.headsuptaichung.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.adapter.viewholder.SavedLocationViewHolder;
import com.base444.android.headsuptaichung.model.SaveLocation;

import java.util.List;

public class SavedLocationAdapter extends RecyclerView.Adapter <SavedLocationViewHolder> {

    private List<SaveLocation> saveLocationList;

    public SavedLocationAdapter(List<SaveLocation> saveLocationList) {
        this.saveLocationList = saveLocationList;
    }

    @NonNull
    @Override
    public SavedLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View locationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        return new ItemViewHolder(locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedLocationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
