package com.example.map_test1.view;

import android.app.MediaRouteActionProvider;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.map_test1.MapsActivity;
import com.example.map_test1.R;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class YearFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_year, container, false);
        Log.v("container id ", v.toString());

        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.numPickerYear);
        numberPicker.setMinValue(MapsActivity.getYears()[0]);
        numberPicker.setMaxValue(MapsActivity.getYears()[MapsActivity.getYears().length-1]);
        numberPicker.setValue(MapsActivity.getCurrentYear());

        AtomicInteger year = new AtomicInteger(MapsActivity.getCurrentYear());
        numberPicker.setOnValueChangedListener(
            (NumberPicker numPicker, int i, int i2) -> year.set(numPicker.getValue())
        );

        Button button = (Button) v.findViewById(R.id.select_year_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapsActivity.setCurrentYear(year.intValue());
                Log.d("btn clicked", "min = " + Integer.toString(numberPicker.getMinValue()) + " max = " + Integer.toString(numberPicker.getMaxValue()) + " currentYear = " + MapsActivity.getCurrentYear());

                Navigation.findNavController(v).navigate(R.id.action_year_to_maps);
            }
        });
        return v;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        NumberPicker numberPicker = (NumberPicker) getView().findViewById(R.id.numPickerYear);
//        numberPicker.setMinValue(2015);
//        numberPicker.setMinValue(2022);
//        // or  (ImageView) view.findViewById(R.id.foo);
//    }
}