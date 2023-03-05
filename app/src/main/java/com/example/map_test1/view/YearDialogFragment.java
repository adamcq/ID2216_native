package com.example.map_test1.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.map_test1.R;
import com.example.map_test1.model.Utils;
import com.example.map_test1.databinding.FragmentYearBinding;

public class YearDialogFragment extends DialogFragment {

    private int latestNumPickerYear;
    FragmentYearBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentYearBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_year, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupListeners(view);
    }

    private void setupView(View view) {
        // set the NumberPicker values
        Log.d("info", "setUpView called");
        NumberPicker numberPicker = view.findViewById(R.id.numPickerYear);
        numberPicker.setMinValue(Utils.getYears()[0]);
        numberPicker.setMaxValue(Utils.getYears()[Utils.getYears().length-1]);
        numberPicker.setValue(Utils.getCurrentYear());
        latestNumPickerYear = Utils.getCurrentYear();
//        view.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setupListeners(View view) {
        // Select button click
        Button selectYearBtn = view.findViewById(R.id.select_year_btn);
        selectYearBtn.setOnClickListener(btn ->  {
            if (latestNumPickerYear != Utils.getCurrentYear()) {
                Utils.setCurrentYear(latestNumPickerYear);
                Utils.updateMap();
                Utils.updateChart();
            }
            dismiss();
        });

        // NumberPicker scroll
        NumberPicker numberPicker = view.findViewById(R.id.numPickerYear);
        numberPicker.setOnValueChangedListener(
                (numPicker, i, i2) -> latestNumPickerYear = numPicker.getValue()
        );
    }



}