package com.example.map_test1;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.map_test1.databinding.ActivityMapsBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.map_test1.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set starting year
        Utils.setCurrentYear(2021);
        Log.v("setValues", "all values set");

        // create district to index mapping
        for (int d = 0; d < Utils.getDistricts().length; d++) {
            Utils.districtToIndex.put(Utils.getDistricts()[d], d);
        }

        // menu layout
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ImageView imgMenu = findViewById(R.id.imgMenu);
        NavigationView navView = findViewById(R.id.navDrawer);

        navView.setItemIconTintList(null);
        imgMenu.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        );

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(navView,navController);

        TextView textTitle = findViewById(R.id.title);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    Log.v("dstID","currentYear: " + Utils.getCurrentYear() + " mapsId: " + Integer.toString(R.id.maps) +  " dst ID: " + Integer.toString(destination.getId()) + " end");
                    if (destination.getId() == R.id.maps) {
                        destination.setLabel(Integer.toString(Utils.getCurrentYear()));
                    }
                    else if (destination.getId() == R.id.chartFragment) {
                        destination.setLabel(Utils.getCurrentDistrict() + " in " + Utils.getCurrentYear());
                    }
                    textTitle.setText(destination.getLabel());
                });
    }
}