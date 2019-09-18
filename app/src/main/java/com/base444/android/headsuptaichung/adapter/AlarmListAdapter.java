package com.base444.android.headsuptaichung.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.adapter.viewholder.AlarmListViewHolder;
import com.base444.android.headsuptaichung.model.AlarmItem;

import java.util.List;

import io.realm.Realm;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListViewHolder> {
    private List<AlarmItem> alarmItems;

    public AlarmListAdapter() {
        Realm realm = Realm.getDefaultInstance();
        this.alarmItems = realm.where(AlarmItem.class).findAll();
    }

    @NonNull
    @Override
    public AlarmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View alarmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new AlarmListViewHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmListViewHolder holder, int position) {
        holder.setItemView(alarmItems.get(position));
    }

    @Override
    public int getItemCount() {
        return alarmItems.size();
    }

    private void onCheckChange(AlarmItem alarmItem , boolean isCheck){
        alarmItem.setEnable(isCheck);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(alarmItem);
        realm.commitTransaction();
    }

}
