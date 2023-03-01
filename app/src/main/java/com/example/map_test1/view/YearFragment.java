package com.example.map_test1.view;

import android.app.AlertDialog;
import android.app.Dialog;
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
import androidx.fragment.app.FragmentActivity;

import com.example.map_test1.R;
import com.example.map_test1.Utils;
import com.example.map_test1.databinding.FragmentYearBinding;

import java.util.concurrent.atomic.AtomicInteger;

public class YearFragment extends DialogFragment {

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



//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            binding = FragmentYearBinding.inflate(getLayoutInflater());
//        }
//
//        selectedItems = new ArrayList();  // Where we track the selected items
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Set the dialog title
//        builder.setTitle(R.string.pick_toppings)
//                // Specify the list array, the items to be selected by default (null for none),
//                // and the listener through which to receive callbacks when items are selected
//                .setMultiChoiceItems(R.array.toppings, null,
//                        new DialogInterface.OnMultiChoiceClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which,
//                                                boolean isChecked) {
//                                if (isChecked) {
//                                    // If the user checked the item, add it to the selected items
//                                    selectedItems.add(which);
//                                } else if (selectedItems.contains(which)) {
//                                    // Else, if the item is already in the array, remove it
//                                    selectedItems.remove(which);
//                                }
//                            }
//                        })
//                // Set the action buttons
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked OK, so save the selectedItems results somewhere
//                        // or return them to the component that opened the dialog
//                        // TODO
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // TODO
//                    }
//                });
//
//        return builder.create();
//    }
// ------- OLD VERSION ---------
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_year, container, false);
//
//        // set the NumberPicker values
//        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.numPickerYear);
//        numberPicker.setMinValue(Utils.getYears()[0]);
//        numberPicker.setMaxValue(Utils.getYears()[Utils.getYears().length-1]);
//        numberPicker.setValue(Utils.getCurrentYear());
//
//        // NumberPicker scroll
//        AtomicInteger year = new AtomicInteger(Utils.getCurrentYear());
//        numberPicker.setOnValueChangedListener(
//            (NumberPicker numPicker, int i, int i2) -> year.set(numPicker.getValue())
//        );
//
//        // Button to save changes made by NumberPicker and go back to MapsFragment
//        Button button = (Button) v.findViewById(R.id.select_year_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Utils.setCurrentYear(year.intValue());
//                Navigation.findNavController(v).navigate(R.id.action_year_to_maps);
//            }
//        });
//        return v;
//    }
}