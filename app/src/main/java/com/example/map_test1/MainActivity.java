package com.example.map_test1;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.map_test1.databinding.ActivityMainBinding;
import com.example.map_test1.view.CrimeDialogFragment;
import com.example.map_test1.view.InfoDialogFragment;
import com.example.map_test1.view.YearDialogFragment;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding binding;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initMappings();
        initAnimations();
        setOnClickListeners();
    }

    private void initMappings() {
        // create district to index mapping
        for (int d = 0; d < Utils.getDistricts().length; d++)
            Utils.districtToIndex.put(Utils.getDistricts()[d], d);
    }

    private void initAnimations() {
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
    }

    private void setOnClickListeners() {
        binding.optionsBtn.setOnClickListener(view -> onAnyButtonClicked());
        binding.crimeBtn.setOnClickListener(view ->
        {
            onAnyButtonClicked();
            DialogFragment crimeFragment = new CrimeDialogFragment();
            crimeFragment.show(getSupportFragmentManager(),"showCrimeFragment");
        });
        binding.infoBtn.setOnClickListener(view ->  {
            onAnyButtonClicked();
            DialogFragment infoFragment = new InfoDialogFragment();
            infoFragment.show(getSupportFragmentManager(), "showInfoFragment");
        });
        binding.yearBtn.setOnClickListener(view ->
        {
            onAnyButtonClicked();
            DialogFragment yearFragment = new YearDialogFragment();
            yearFragment.show(getSupportFragmentManager(),"showYearFragment");
        });
    }

    private void onAnyButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            Log.d("info", "setVisibility");
            binding.crimeBtn.setVisibility(View.VISIBLE);
            binding.infoBtn.setVisibility(View.VISIBLE);
            binding.yearBtn.setVisibility(View.VISIBLE);
        }
        else {
            binding.crimeBtn.setVisibility(View.INVISIBLE);
            binding.infoBtn.setVisibility(View.INVISIBLE);
            binding.yearBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            Log.d("info", "setAnimation");
            binding.crimeBtn.setAnimation(fromBottom);
            binding.infoBtn.setAnimation(fromBottom);
            binding.yearBtn.setAnimation(fromBottom);
            binding.optionsBtn.setAnimation(rotateOpen);
        }
        else {
            binding.crimeBtn.setAnimation(toBottom);
            binding.infoBtn.setAnimation(toBottom);
            binding.yearBtn.setAnimation(toBottom);
            binding.optionsBtn.setAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            binding.crimeBtn.setClickable(true);
            binding.yearBtn.setClickable(true);
            binding.infoBtn.setClickable(true);
        }
        else {
            binding.crimeBtn.setClickable(false);
            binding.yearBtn.setClickable(false);
            binding.infoBtn.setClickable(false);
        }
    }
}