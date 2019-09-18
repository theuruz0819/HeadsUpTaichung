package com.base444.android.headsuptaichung.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base444.android.headsuptaichung.R;
import com.base444.android.headsuptaichung.model.AlarmItem;

public class AlarmListViewHolder extends RecyclerView.ViewHolder{
    private TextView timeTextView;
    private CheckBox enableCheckBox;
    public AlarmListViewHolder(@NonNull View itemView) {
        super(itemView);
        timeTextView = itemView.findViewById(R.id.alarm_list_item_time_text);
        enableCheckBox = itemView.findViewById(R.id.alarm_list_item_enable_checkbox);
    }
    public void setItemView(AlarmItem alarmItem){
        timeTextView.setText(String.valueOf(alarmItem.getHour()) + " : " + String.valueOf(alarmItem.getMinute()));
        enableCheckBox.setChecked(alarmItem.getEnable());
    }
}
