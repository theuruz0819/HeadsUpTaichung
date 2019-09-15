package com.base444.android.headsuptaichung.adapter.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.adapter.SavedLocationAdapter;
import com.base444.android.headsuptaichung.asynctask.NearByCaseTask;
import com.base444.android.headsuptaichung.model.SaveLocation;
import com.google.android.gms.maps.model.LatLng;

public class SavedLocationViewHolder extends RecyclerView.ViewHolder {
    TextView markerName;
    TextView markerNote;
    ImageButton deleteBtn;
    TextView distanceMessage;
    SaveLocation saveLocation;
    SavedLocationAdapter.OnLocationInteract onLocationInteract;

    public SavedLocationViewHolder(@NonNull View itemView) {
        super(itemView);
        markerName = itemView.findViewById(R.id.location_list_item_title);
        markerNote = itemView.findViewById(R.id.location_list_item_note);
        deleteBtn = itemView.findViewById(R.id.location_list_item_delete_btn);
        distanceMessage = itemView.findViewById(R.id.location_list_item_near_by_message);
    }

    public void setLocation(final SaveLocation saveLocation, SavedLocationAdapter.OnLocationInteract locationListener, Activity context) {
        this.onLocationInteract = locationListener;
        this.saveLocation = saveLocation;
        markerName.setText(saveLocation.getName());
        markerNote.setText(saveLocation.getNote());
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationInteract.onDeleteBtnClickListener(saveLocation);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationInteract.onItemClickListener(saveLocation);
            }
        });
        distanceMessage.setText("Processing...");
        NearByCaseTask task = new NearByCaseTask(distanceMessage, context);
        LatLng targetPoint = new LatLng(saveLocation.getLatitude(), saveLocation.getLongitude());
        task.execute(targetPoint);
    }
}
