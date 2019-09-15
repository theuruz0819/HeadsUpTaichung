package com.base444.android.headsuptaichung.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.adapter.viewholder.SavedLocationViewHolder;
import com.base444.android.headsuptaichung.model.SaveLocation;

import java.util.List;

import io.realm.Realm;

public class SavedLocationAdapter extends RecyclerView.Adapter <SavedLocationViewHolder> {

    private List<SaveLocation> saveLocationList;
    private OnLocationInteract onLocationInteract;
    private Activity context;

    public interface OnLocationInteract{
        void onDeleteBtnClickListener(SaveLocation saveLocation);
        void onItemClickListener(SaveLocation saveLocation);
    }

    public SavedLocationAdapter(Activity context) {
        this.context = context;
        final Realm realm = Realm.getDefaultInstance();
        saveLocationList = realm.where(SaveLocation.class).findAll();

        onLocationInteract = new OnLocationInteract() {
            @Override
            public void onDeleteBtnClickListener(SaveLocation saveLocation) {

                realm.beginTransaction();
                saveLocation.deleteFromRealm();
                realm.commitTransaction();
                saveLocationList = realm.where(SaveLocation.class).findAll();
                notifyDataSetChanged();
            }

            @Override
            public void onItemClickListener(SaveLocation saveLocation) {

            }
        };
    }

    @NonNull
    @Override
    public SavedLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View locationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        return new SavedLocationViewHolder(locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedLocationViewHolder holder, int position) {
        holder.setLocation(saveLocationList.get(position), onLocationInteract, context);
    }

    @Override
    public int getItemCount() {
        return saveLocationList.size();
    }
}
