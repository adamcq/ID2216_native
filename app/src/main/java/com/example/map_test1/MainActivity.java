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
import com.example.map_test1.view.YearFragment;

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

        // set starting year
        Utils.setCurrentYear(2021);
        Log.v("setValues", "all values set");

        // create district to index mapping
        for (int d = 0; d < Utils.getDistricts().length; d++) {
            Utils.districtToIndex.put(Utils.getDistricts()[d], d);
        }

        // initialize values in crimeList (in crimeDialogFragment)


        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        binding.optionsBtn.setOnClickListener(view -> onAnyButtonClicked());
        binding.crimeBtn.setOnClickListener(view ->
                {
                    onAnyButtonClicked();
                    Toast.makeText(this, "CrimeBtn clicked", Toast.LENGTH_SHORT).show();
                    DialogFragment crimeFragment = new CrimeDialogFragment();
                    crimeFragment.show(getSupportFragmentManager(),"showCrimeFragment");
                });
        binding.infoBtn.setOnClickListener(view -> Toast.makeText(this, "InfoBtn clicked", Toast.LENGTH_SHORT).show());
        binding.yearBtn.setOnClickListener(view ->
                {
                    onAnyButtonClicked();
                    Toast.makeText(this, "YearBtn clicked", Toast.LENGTH_SHORT).show();
                    DialogFragment yearFragment = new YearFragment();
                    yearFragment.show(getSupportFragmentManager(),"showYearFragment");
                });
//    ------------------------- OLD VERSION -------------------------
//        // menu layout
//        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
//        ImageView imgMenu = findViewById(R.id.imgMenu);
//        NavigationView navView = findViewById(R.id.navDrawer);
//
//        navView.setItemIconTintList(null);
//        imgMenu.setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            }
//        );
//
//        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
//        NavigationUI.setupWithNavController(navView,navController);
//
//        TextView textTitle = findViewById(R.id.title);
//        navController.addOnDestinationChangedListener(
//                (controller, destination, arguments) -> {
//                    Log.v("dstID","currentYear: " + Utils.getCurrentYear() + " mapsId: " + Integer.toString(R.id.maps) +  " dst ID: " + Integer.toString(destination.getId()) + " end");
//                    if (destination.getId() == R.id.maps) {
//                        destination.setLabel(Integer.toString(Utils.getCurrentYear()));
//                        Log.d("dstInfo", "maps!!");
//                    }
//                    else if (destination.getId() == R.id.chartFragment) {
//                        destination.setLabel(Utils.getCurrentDistrict() + " in " + Utils.getCurrentYear());
//                    }
//                    textTitle.setText(destination.getLabel());
//                });
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