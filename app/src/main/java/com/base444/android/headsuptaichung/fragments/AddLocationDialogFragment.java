package com.base444.android.headsuptaichung.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.base444.android.headsuptaichung.R;

public class AddLocationDialogFragment extends DialogFragment {

    private MarkerSaveInterFace markerSaveInterFace;
    public interface MarkerSaveInterFace{
        void onSaveButtonClick(String name, String note);
    }
    public AddLocationDialogFragment(MarkerSaveInterFace markerSaveInterFace) {
        this.markerSaveInterFace = markerSaveInterFace;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_location_dailog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button saveButton = view.findViewById(R.id.add_location_fragment_save_btn);
        Button cancelButton = view.findViewById(R.id.add_location_fragment_cancel_btn);
        final EditText nameEdt = view.findViewById(R.id.add_location_fragment_marker_name_edt);
        final EditText noteEdt = view.findViewById(R.id.add_location_fragment_marker_note_edt);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerSaveInterFace != null) {
                    markerSaveInterFace.onSaveButtonClick(nameEdt.getText().toString(), noteEdt.getText().toString());
                }
            }
        });
    }
}
