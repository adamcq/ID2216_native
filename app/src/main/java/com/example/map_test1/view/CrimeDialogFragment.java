package com.example.map_test1.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.map_test1.custom.CrimeAdapter;
import com.example.map_test1.databinding.DialogFragmentCrimeBinding;
import com.example.map_test1.model.CrimeItem;
import com.example.map_test1.viewModel.SharedViewModel;

import java.util.ArrayList;

public class CrimeDialogFragment extends DialogFragment {
    DialogFragmentCrimeBinding binding;
    private SharedViewModel mSharedViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentCrimeBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();
        RecyclerView crimesRV = binding.crimeSelectionRecyclerView;

        // Here, we have created new array list and added data to it
        ArrayList<CrimeItem> crimeItemArrayList = new ArrayList<CrimeItem>();
        for (String crimeName : mSharedViewModel.getCrimes().getValue()) {
            crimeItemArrayList.add(new CrimeItem(crimeName));
        }

        // we are initializing our adapter class and passing our arraylist to it.
        CrimeAdapter crimeAdapter = new CrimeAdapter(getContext(), crimeItemArrayList, mSharedViewModel);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        crimesRV.setLayoutManager(linearLayoutManager);
        crimesRV.setAdapter(crimeAdapter);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setOnClickListeners() {
        binding.confirmCrimeSelectionBtn.setOnClickListener(btn -> dismiss());
    }
}