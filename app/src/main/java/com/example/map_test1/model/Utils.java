package com.example.map_test1.model;


import android.annotation.SuppressLint;

import com.example.map_test1.view.MapsFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static int currentYear = 2022;
    private static String currentDistrict;
    public static Map<String, Integer> districtToIndex = new HashMap<String, Integer>();

    /**
     * data to be displayed
     */
    private static boolean[] currentCrimes = new boolean[] {
            true, true, true, true, true, true, true, true, true, true, true, true
    };

    private static int maxCrimeCount;
    public static int getMaxCrimeCount() {
        return maxCrimeCount;
    }

    private static int[] crimeCounts;
    public static int[] getCrimeCounts() {
        return crimeCounts;
    }
    public static void updateCrimeCounts() {
        crimeCounts = new int[Data.districts.length];
        maxCrimeCount = 0;
        for (int d = 0; d < Data.districts.length; d++) {
            int crimeCount = 0;
            for (int c = 0; c < Data.crimes.length; c++)
                if (currentCrimes[c])
                    crimeCount += Data.data[d][getCurrentYearIndex()][c];
            crimeCounts[d] = crimeCount; // update crime count

            if (crimeCounts[d] > maxCrimeCount) // update max crime count
                maxCrimeCount = crimeCounts[d];
        }
    }

    public static boolean[] getCurrentCrimes() {
        return currentCrimes;
    }
    public static void setCurrentCrimes(boolean[] newCrimes) {
        currentCrimes = newCrimes;
    }

    private static GeoJsonLayer layer;
    public static GeoJsonLayer getLayer() {
        return layer;
    }
    public static void setLayer(GeoJsonLayer newLayer) {
        layer = newLayer;
    }

    public static int[] getYears() {
        return Data.years;
    }
    public static int getCurrentYear(){
        return currentYear;
    }

    public static String getCurrentDistrict(){
        return currentDistrict;
    }

    public static int getCurrentYearIndex() {
        return currentYear - Data.years[0];
    }

    public static Integer getCurrentDistrictIndex(){
        return districtToIndex.get(currentDistrict);
    }

    public static String[] getDistricts() {
        return Data.districts;
    }
    public static String[] getCrimes(){
        return Data.crimes;
    }

    public static int[][][] getData(){
        return Data.data;
    }

    public static void setCurrentYear(int year) {
        currentYear = year;
    }

    public static void setCurrentDistrict(String district) {
        currentDistrict = district;
    }

    @SuppressLint("SetTextI18n")
    public static void updateMap() {
        updateCrimeCounts();
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setPolygonStyle(Utils.getLayer());
    }

    public static void updateChart() {
        // TODO
    }

    // CHART CONFIG
    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    public BarChart getBarChart() {
        return barChart;
    }

    public void setBarChart(BarChart barChart) {
        this.barChart = barChart;
    }

    public BarData getBarData() {
        return barData;
    }

    public void setBarData(BarData barData) {
        this.barData = barData;
    }

    public BarDataSet getBarDataSet() {
        return barDataSet;
    }

    public void setBarDataSet(BarDataSet barDataSet) {
        this.barDataSet = barDataSet;
    }
}
