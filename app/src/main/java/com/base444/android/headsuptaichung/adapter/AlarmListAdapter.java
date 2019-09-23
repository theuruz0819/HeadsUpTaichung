package com.base444.android.headsuptaichung.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.AlarmUtil;
import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.adapter.viewholder.AlarmListViewHolder;
import com.base444.android.headsuptaichung.model.AlarmItem;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListViewHolder> {
    private List<AlarmItem> alarmItems;
    private Context context;

    public AlarmListAdapter(Context context) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull AlarmListViewHolder holder, final int position) {
        holder.setItemView(alarmItems.get(position));
        holder.enableCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                onCheckChange(alarmItems.get(position), isCheck);
            }
        });
        holder.itemRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmUtil.cancel_alarm(context, alarmItems.get(position).getRequestId());
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                alarmItems.get(position).deleteFromRealm();
                realm.commitTransaction();
                dataUpdate();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmItems.size();
    }

    public void dataUpdate(){
        Realm realm = Realm.getDefaultInstance();
        this.alarmItems = realm.where(AlarmItem.class).findAll();
    }

    private void onCheckChange(AlarmItem alarmItem , boolean isCheck){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        alarmItem.setEnable(isCheck);
        realm.copyToRealmOrUpdate(alarmItem);
        realm.commitTransaction();
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > alarmItem.getHour()){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, alarmItem.getHour());
        calendar.set(Calendar.MINUTE, alarmItem.getMinute());

        if (isCheck){
            AlarmUtil.add_alarm(context, calendar, alarmItem.getRequestId(), true);
        } else {
            AlarmUtil.cancel_alarm(context, alarmItem.getRequestId());
        }
    }

}
