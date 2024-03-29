package com.example.map_test1.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.map_test1.R;
import com.example.map_test1.databinding.DialogFragmentInfoBinding;
import com.example.map_test1.viewModel.SharedViewModel;

public class InfoDialogFragment extends DialogFragment {
     DialogFragmentInfoBinding binding;
     private SharedViewModel mSharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogFragmentInfoBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        return inflater.inflate(R.layout.dialog_fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupListeners(view);
    }

    @SuppressLint("SetTextI18n")
    private void setupView(View view) {
        // TODO change the textview values based on crimes selected
        int maxCrimeCount = mSharedViewModel.getCurrentMaxCrimeCount().getValue();
        ((TextView)view.findViewById(R.id.greenLevel)).setText("up to " + maxCrimeCount/4 + " (25%)");
        ((TextView)view.findViewById(R.id.yellowLevel)).setText("up to " + maxCrimeCount/2 + " (50%)");
        ((TextView)view.findViewById(R.id.orangeLevel)).setText("up to " + (int)(maxCrimeCount * 0.75) + " (75%)");
        ((TextView)view.findViewById(R.id.redLevel)).setText("up to " + maxCrimeCount + " (max)");
    }

    private void setupListeners(View view) {
        view.findViewById(R.id.button3).setOnClickListener(btn -> dismiss());
    }

}