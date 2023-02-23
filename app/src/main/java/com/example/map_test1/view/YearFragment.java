package com.example.map_test1.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.map_test1.R;
import com.example.map_test1.Utils;

import java.util.concurrent.atomic.AtomicInteger;

public class YearFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_year, container, false);

        // set the NumberPicker values
        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.numPickerYear);
        numberPicker.setMinValue(Utils.getYears()[0]);
        numberPicker.setMaxValue(Utils.getYears()[Utils.getYears().length-1]);
        numberPicker.setValue(Utils.getCurrentYear());

        // NumberPicker scroll
        AtomicInteger year = new AtomicInteger(Utils.getCurrentYear());
        numberPicker.setOnValueChangedListener(
            (NumberPicker numPicker, int i, int i2) -> year.set(numPicker.getValue())
        );

        // Button to save changes made by NumberPicker and go back to MapsFragment
        Button button = (Button) v.findViewById(R.id.select_year_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.setCurrentYear(year.intValue());
                Navigation.findNavController(v).navigate(R.id.action_year_to_maps);
            }
        });
        return v;
    }
}