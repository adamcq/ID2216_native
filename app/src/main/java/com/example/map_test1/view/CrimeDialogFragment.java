package com.example.map_test1.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.map_test1.R;
import com.example.map_test1.Utils;
import com.example.map_test1.custom.CrimeAdapter;
import com.example.map_test1.custom.CrimeItem;
import com.example.map_test1.databinding.FragmentCrimeDialogBinding;
import com.example.map_test1.databinding.FragmentYearBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class CrimeDialogFragment extends DialogFragment {
    ArrayList selectedItems;
    FragmentCrimeDialogBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrimeDialogBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView courseRV = binding.crimeSelectionRecyclerView;

        // Here, we have created new array list and added data to it
        ArrayList<CrimeItem> crimeItemArrayList = new ArrayList<CrimeItem>();
        for (String crimeName : Utils.getCrimes()) {
            crimeItemArrayList.add(new CrimeItem(crimeName));
        }

        // we are initializing our adapter class and passing our arraylist to it.
        CrimeAdapter crimeAdapter = new CrimeAdapter(getContext(), crimeItemArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(crimeAdapter);
    }



//    ---------------- WORKING VERSION BUT UGLY -----------------
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        selectedItems = new ArrayList();  // Where we track the selected items
//        boolean[] currentSession = Arrays.copyOf(Utils.getCurrentCrimes(), Utils.getCurrentCrimes().length);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Set the dialog title
//        builder.setTitle(R.string.pick_crimes)
//                // Specify the list array, the items to be selected by default (null for none),
//                // and the listener through which to receive callbacks when items are selected
//                .setMultiChoiceItems(R.array.crimeChoicesArray, currentSession,
//                        (dialog, item, isChecked) -> {
//                            Log.d("item", "current crimes = " + Arrays.toString(Utils.getCurrentCrimes()));
//                            Log.d("item", "selected items = " + Arrays.toString(selectedItems.toArray()));
//                            if (isChecked) {
//                                // If the user checked the item, add it to the selected items
//                                selectedItems.add(item);
//                            } else if (selectedItems.contains(item)) {
//                                // Else, if the item is already in the array, remove it
//                                selectedItems.remove((Object)item);
//                            }
//                            Log.d("item", "current crimes = " + Arrays.toString(Utils.getCurrentCrimes()));
//                            Log.d("item", "selected items = " + Arrays.toString(selectedItems.toArray()));
//                        })
//                // Set the action buttons
//                .setPositiveButton(R.string.ok, (dialog, id) -> Utils.setCurrentCrimes(currentSession))
//                .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());
//
//        return builder.create();
//    }


}