package com.example.map_test1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.map_test1.R;
import com.example.map_test1.Utils;
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
import java.util.Objects;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    final private int[] CRIME_LEVEL_COLORS = new int[]{0x7799FF33, 0x77FFFF33, 0x77FF9900, 0x77FF0000};
    private final static String mLogTag = "MapsFragment";
//    private int currentYear = 2022;
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
        v = inflater.inflate(R.layout.fragment_maps, container, false);
        return v;
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
        Log.v("retrieved map for ", Integer.toString(Utils.getCurrentYear()));
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.neighbourhoods, getContext());
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
        // TODO change fragment to ShowData onClick
        layer.setOnFeatureClickListener(feature -> {
                Utils.setCurrentDistrict(feature.getProperty("neighbourhood"));
                Navigation.findNavController(v).navigate(R.id.action_maps_to_chartFragment);
        });
//                Toast.makeText(thisContext,
//                "Feature clicked: " + feature.getProperty("neighbourhood"),
//                Toast.LENGTH_SHORT).show());
    }

    private void setPolygonStyle(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : layer.getFeatures()) {
            // get and set color for district for year-district pair
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
            int districtIndex = Utils.districtToIndex.get(feature.getProperty("neighbourhood"));
            int crimeCount = getCrimeCount(getYearIndexByYear(Utils.getCurrentYear()), districtIndex);
            int color = getColorByCrimeCount(crimeCount);

            Log.v("crimeCountInfo", Utils.getCurrentYear() + " " + Utils.getDistricts()[districtIndex] + " " + crimeCount + feature.getProperty("neighbourhood"));

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
            crimeCount += Utils.getData()[d][y][c];
        }
        return crimeCount;
    }

    private int getDistrictIndexByName(String name) {
        String[] districts = Utils.getDistricts();
        for (int i = 0; i < districts.length; i++) {
            if (Objects.equals(districts[i], name))
                return i;
        }
        return -1;
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

    public int getYearIndexByYear(int year) {
        return year - 2015;
    }
}