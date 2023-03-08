package com.example.map_test1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.map_test1.R;
import com.example.map_test1.viewModel.SharedViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    final private int[] CRIME_LEVEL_COLORS = new int[]{0x7799FF33, 0x77FFFF33, 0x77FF9900, 0x77FF0000};
    private final static String mLogTag = "MapsFragment";
    private View v;
    private SharedViewModel mSharedViewModel;
    final private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;

            // Move camera to Stockholm with a zoom
            LatLng stockholm = new LatLng(59.3312, 17.99);
            float zoom = (float)10.2;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stockholm, zoom));
            retrieveFileFromResource();
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_maps, container, false);

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Button currentYearBtn = v.findViewById(R.id.incrementYearButton);
        mSharedViewModel.getCurrentYear().observe(getViewLifecycleOwner(), year -> {
            currentYearBtn.setText(Integer.toString(year));
            try {
                updateMap();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        mSharedViewModel.getCurrentCrimeCounts().observe(getViewLifecycleOwner(), crimes -> {
            try {
                updateMap();
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initMap();
        setupView(view);
    }

    @SuppressLint("SetTextI18n")
    private void setupView(View view) {
        mSharedViewModel.updateCurrentCrimeCounts();

        Button button = view.findViewById(R.id.incrementYearButton);
        button.setText(Integer.toString(mSharedViewModel.getCurrentYear().getValue()));

        button.setOnClickListener(v -> {
            mSharedViewModel.incrementYear();
            button.setText(Integer.toString(mSharedViewModel.getCurrentYear().getValue()));
            updateMap();
        });
    }

    @SuppressLint("SetTextI18n")
    public void updateMap() {
        Log.d("SVM info", String.valueOf(mSharedViewModel.getLayer().getValue()));
        setPolygonStyle(mSharedViewModel.getLayer().getValue());
    }

    private void initMap() {
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); // getSupportFragmentManager()
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }
    }

    private void retrieveFileFromResource() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.neighbourhoods, requireContext());
            Log.d("SVM info", String.valueOf(layer));
            mSharedViewModel.setLayer(layer);
            addGeoJsonLayerToMap(layer);
        } catch (IOException e) {
            Log.e(mLogTag, "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
        }
    }
    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {
        setPolygonStyle(layer);
        layer.removeLayerFromMap();
        layer.addLayerToMap();
        layer.setOnFeatureClickListener(feature -> {
                mSharedViewModel.updateCurrentDistrict(feature.getProperty("neighbourhood"));
                Navigation.findNavController(v).navigate(R.id.action_maps_to_chartFragment);
        });
    }

    public void setPolygonStyle(GeoJsonLayer layer) {
        Log.d("info", mSharedViewModel.toString());
        Integer[] crimeCounts = mSharedViewModel.getCurrentCrimeCounts().getValue();
        for (GeoJsonFeature feature : layer.getFeatures()) {

            // get and set color for district for year-district pair
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
            int d = mSharedViewModel.findDistrictIndexByName(feature.getProperty("neighbourhood"));
            int color = getColorByCrimeCount(crimeCounts[d]);

            polygonStyle.setFillColor(color);
            polygonStyle.setStrokeWidth(2);
            feature.setPolygonStyle(polygonStyle);
        }
    }

        private int getColorByCrimeCount(int crimeCount) {
        int maxCrimeCount = mSharedViewModel.getCurrentMaxCrimeCount().getValue();
        if (crimeCount < maxCrimeCount / 4) {
            return CRIME_LEVEL_COLORS[0];
        } else if (crimeCount < maxCrimeCount / 2) {
            return CRIME_LEVEL_COLORS[1];
        } else if (crimeCount < 0.75 * maxCrimeCount) {
            return CRIME_LEVEL_COLORS[2];
        } else {
            return CRIME_LEVEL_COLORS[3];
        }
    }
}