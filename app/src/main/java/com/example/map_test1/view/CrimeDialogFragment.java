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
        setOnClickListeners();
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Utils.updateCrimeCounts();
        Utils.updateMap();
        Utils.updateChart();
    }

    public void setOnClickListeners() {
        binding.confirmCrimeSelectionBtn.setOnClickListener(btn -> dismiss());
    }
}