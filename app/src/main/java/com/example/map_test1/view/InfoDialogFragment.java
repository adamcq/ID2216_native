package com.example.map_test1.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.map_test1.R;
import com.example.map_test1.databinding.FragmentInfoBinding;

public class InfoDialogFragment extends DialogFragment {
     FragmentInfoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupListeners(view);
    }

    private void setupView(View view) {
        // TODO change the textview values based on crimes selected
    }

    private void setupListeners(View view) {
        view.findViewById(R.id.button3).setOnClickListener(btn -> dismiss());
    }

}