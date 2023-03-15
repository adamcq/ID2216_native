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
import androidx.lifecycle.ViewModelProvider;

import com.example.map_test1.R;
import com.example.map_test1.databinding.DialogFragmentYearBinding;
import com.example.map_test1.viewModel.SharedViewModel;

public class YearDialogFragment extends DialogFragment {

    private int latestNumPickerYear;
    DialogFragmentYearBinding binding;
    SharedViewModel mSharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogFragmentYearBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        return inflater.inflate(R.layout.dialog_fragment_year, container, false);
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
        Integer[] years = mSharedViewModel.getYears().getValue();
        Integer currentYear = mSharedViewModel.getCurrentYear().getValue();

        NumberPicker numberPicker = view.findViewById(R.id.numPickerYear);
        numberPicker.setMinValue(years[0]);
        numberPicker.setMaxValue(years[years.length-1]);
        numberPicker.setValue(currentYear);
        latestNumPickerYear = currentYear;
//        view.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setupListeners(View view) {
        // Select button click
        Button selectYearBtn = view.findViewById(R.id.select_year_btn);
        selectYearBtn.setOnClickListener(btn ->  {
            if (latestNumPickerYear != mSharedViewModel.getCurrentYear().getValue()) {
                mSharedViewModel.updateCurrentYear(latestNumPickerYear);
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