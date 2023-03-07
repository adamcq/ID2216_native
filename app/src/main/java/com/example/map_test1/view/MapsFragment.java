package com.example.map_test1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.map_test1.R;
import com.example.map_test1.model.Utils;
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
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
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
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initMap();
        setupView(view);
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

    @SuppressLint("SetTextI18n")
    private void setupView(View view) {
        Utils.updateCrimeCounts();

        Button button = view.findViewById(R.id.button);
        button.setText(Integer.toString(Utils.getCurrentYear()));
        button.setOnClickListener(v -> {
            Utils.setCurrentYear((Utils.getCurrentYear() + 2) % 8 + 2015);
            button.setText(Integer.toString(Utils.getCurrentYear()));
            Utils.updateMap();
        });
    }

    private void retrieveFileFromResource() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.neighbourhoods, getContext());
            Utils.setLayer(layer);
            addGeoJsonLayerToMap(layer);
        } catch (IOException e) {
            Log.e(mLogTag, "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
        }
    }
    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {
        setPolygonStyle(layer);
        layer.addLayerToMap();
        layer.setOnFeatureClickListener(feature -> {
                Utils.setCurrentDistrict(feature.getProperty("neighbourhood"));
                Navigation.findNavController(v).navigate(R.id.action_maps_to_chartFragment);
        });
    }

    public void setPolygonStyle(GeoJsonLayer layer) {
        int[] crimeCounts = Utils.getCrimeCounts();
        for (GeoJsonFeature feature : layer.getFeatures()) {

            // get and set color for district for year-district pair
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
            int d = Utils.districtToIndex.get(feature.getProperty("neighbourhood"));
            int color = getColorByCrimeCount(crimeCounts[d]);

            polygonStyle.setFillColor(color);
            polygonStyle.setStrokeWidth(2);
            feature.setPolygonStyle(polygonStyle);
        }
    }

    /**
     * @param y year index
     * @param d district index
     * @return crime count for given year-district pair based on data
     */
    private static int getCrimeCount(int y, int d) {
        int crimeCount = 0;
        for (int c = 0; c < Utils.getCrimes().length; c++) {
            if (Utils.getCurrentCrimes()[c]) // check if it is in the selection
                crimeCount += Utils.getData()[d][y][c];
        }
        return crimeCount;
    }

    /**
     * @param crimeCount the amount of crimes
     * @return color based on crimeCount (the bigger the more red)
     */
    private int getColorByCrimeCount(int crimeCount) {
        if (crimeCount < Utils.getCurrentMaxCrimeCount() / 4) {
            return CRIME_LEVEL_COLORS[0];
        } else if (crimeCount < Utils.getCurrentMaxCrimeCount() / 2) {
            return CRIME_LEVEL_COLORS[1];
        } else if (crimeCount < 0.75 * Utils.getCurrentMaxCrimeCount()) {
            return CRIME_LEVEL_COLORS[2];
        } else {
            return CRIME_LEVEL_COLORS[3];
        }
    }

    public int getYearIndexByYear(int year) {
        return year - 2015;
    }
}