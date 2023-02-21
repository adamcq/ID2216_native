package com.example.map_test1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.map_test1.MapsActivity;
import com.example.map_test1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    final private int[] CRIME_LEVEL_COLORS = new int[]{0x7799FF33, 0x77FFFF33, 0x77FF9900, 0x77FF0000};
    private final static String mLogTag = "MapsFragment";
//    private int currentYear = 2022;
    Context thisContext;
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
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Move camera to Stockholm with a zoom
            LatLng stockholm = new LatLng(59.3312, 18.0160);
            float zoom = (float)10.7;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stockholm, zoom));

            retrieveFileFromResource();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // store the context
        thisContext = container.getContext();
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); // getSupportFragmentManager()
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void retrieveFileFromResource() {
        Log.v("retrieved map for ", Integer.toString(MapsActivity.getCurrentYear()));
        try {
            GeoJsonLayer layer = new GeoJsonLayer(getMap(), R.raw.neighbourhoods, thisContext);
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
        // Demonstrate receiving features via GeoJsonLayer clicks.
        // TODO change to our click listener
//        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
//            @Override
//            public void onFeatureClick(Feature feature) {
//                Toast.makeText(MapsActivity.this,
//                        "Feature clicked: " + feature.getProperty("title"),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        });
    }

    private void setPolygonStyle(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : layer.getFeatures()) {
            // get and set color for district for year-district pair
            int districtIndex = getDistrictIndexByName(feature.getProperty("neighbourhood"));
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
//            int crimeCount = getCrimeCount(getYearIndexByYear(MapsActivity.getCurrentYear()), districtIndex);
            int crimeCount = MapsActivity.getCrimeCounts()[MapsActivity.getCurrentYear()-MapsActivity.getYears()[0]][districtIndex];
            int color = getColorByCrimeCount(crimeCount);
            Log.v("crimeCountInfo", MapsActivity.getCurrentYear() + " " + MapsActivity.getDistricts()[districtIndex] + " " + crimeCount + feature.getProperty("neighbourhood"));
            polygonStyle.setFillColor(color);
            polygonStyle.setStrokeWidth(2);
            feature.setPolygonStyle(polygonStyle);
        }
    }

    private int getDistrictIndexByName(String name) {
        String[] districts = MapsActivity.getDistricts();
        for (int i = 0; i < districts.length; i++) {
            if (Objects.equals(districts[i], name))
                return i;
        }
        return 0;
    }

    /**
     * @param crimeCount the amount of crimes
     * @return color based on crimeCount (the bigger the more red)
     */
    private int getColorByCrimeCount(int crimeCount) {
        if (crimeCount < 2500) {
            return CRIME_LEVEL_COLORS[0];
        } else if (crimeCount < 5000) {
            return CRIME_LEVEL_COLORS[1];
        } else if (crimeCount < 7500) {
            return CRIME_LEVEL_COLORS[2];
        } else {
            return CRIME_LEVEL_COLORS[3];
        }
    }

    protected GoogleMap getMap() {
        return mMap;
    }

    public int getYearIndexByYear(int year) {
        return year - 2015;
    }
}