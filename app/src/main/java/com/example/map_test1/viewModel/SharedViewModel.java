package com.example.map_test1.viewModel;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.map_test1.repository.CriminalDataRepository;
import com.example.map_test1.view.MapsFragment;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SharedViewModel extends ViewModel {
    // only loaded once from database
    private MutableLiveData<Integer[]> mYears;
    private MutableLiveData<String[]> mDistricts;
    private MutableLiveData<String[]> mCrimes;
    private MutableLiveData<Integer[][][]> mRecords;


    // constantly updated by the user input - these variables reflect current state to be displayed
    private MutableLiveData<Integer> mCurrentYear = new MutableLiveData<>(2022); // TODO init with method
    private MutableLiveData<String> mCurrentDistrict = new MutableLiveData<>(null); // TODO init with click
    private MutableLiveData<boolean[]> mCurrentCrimes = new MutableLiveData<>( new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true});
    private MutableLiveData<Integer> mCurrentMaxCrimeCount = new MutableLiveData<>(null); // TODO init with method
    private MutableLiveData<Integer[]> mCurrentCrimeCounts = new MutableLiveData<>(null);
    private Map<String, Integer> districtToIndex = new HashMap<String, Integer>();
    private MutableLiveData<GeoJsonLayer> mLayer = new MutableLiveData<>(null);

    CriminalDataRepository mRepo;

    public void init() {
        if (mYears != null && mDistricts != null && mCrimes != null && mRecords != null) {
            return;
        }
        mRepo = CriminalDataRepository.getInstance();
        mYears = mRepo.getYears();
        mDistricts = mRepo.getDistricts();
        mCrimes = mRepo.getCrimes();
        mRecords = mRepo.getRecords();
        initMappings();
    }

    // getters
    public LiveData<Integer> getCurrentYear() {
        return mCurrentYear;
    }

    public LiveData<String> getCurrentDistrict() {
        return mCurrentDistrict;
    }

    public LiveData<boolean[]> getCurrentCrimes() {
        return mCurrentCrimes;
    }

    public LiveData<Integer> getCurrentMaxCrimeCount() {
        return mCurrentMaxCrimeCount;
    }

    public LiveData<Integer[]> getCurrentCrimeCounts() {
        return mCurrentCrimeCounts;
    }

    public LiveData<GeoJsonLayer> getLayer() {
        return mLayer;
    }
    public LiveData<Integer[]> getYears() {
        return mYears;
    }
    public LiveData<String[]> getCrimes() {
        return mCrimes;
    }

    public LiveData<Integer[][][]> getRecords() {
        return mRecords;
    }

    // setters
    public void updateCurrentYear(Integer currentYear) {
        mCurrentYear.setValue(currentYear);
        updateCurrentCrimeCounts();
    }

    public void updateCurrentDistrict(String currentDistrict) {
        mCurrentDistrict.setValue(currentDistrict);
    }

    public void updateCurrentCrimes(boolean[] currentCrimes) {
        mCurrentCrimes.setValue(currentCrimes);
        updateCurrentCrimeCounts();
    }

    public void updateCurrentMaxCrimeCount(Integer currentMaxCrimeCount) {
        mCurrentMaxCrimeCount.setValue(currentMaxCrimeCount);
    }

    public void setLayer(GeoJsonLayer layer) {
        Log.d("SVM info", "layer set " + String.valueOf(mLayer.getValue()));
        mLayer.setValue(layer);
    }

    // LOGIC METHODS


    public void updateCurrentCrimeCounts() {
        Log.d("SVM info", Arrays.toString(mDistricts.getValue()));
        Integer[] crimeCounts = new Integer[mDistricts.getValue().length];
        int maxCrimeCount = 0;
        int y = mCurrentYear.getValue() - mYears.getValue()[0];
        for (int d = 0; d < mDistricts.getValue().length; d++) {
            int crimeCount = 0;
            for (int c = 0; c < mCrimes.getValue().length; c++)
                if (mCurrentCrimes.getValue()[c])
                    crimeCount += mRecords.getValue()[d][y][c];
            crimeCounts[d] = crimeCount; // update crime count

            if (crimeCounts[d] > maxCrimeCount) // update max crime count
                maxCrimeCount = crimeCounts[d];
        }
        mCurrentMaxCrimeCount.setValue(maxCrimeCount);
        mCurrentCrimeCounts.setValue(crimeCounts);
    }

    public void incrementYear() {
        mCurrentYear.setValue((mCurrentYear.getValue() - 2015 + 1) % 8 + 2015);
        updateCurrentCrimeCounts();
    }

    private void initMappings() {
        // create district to index mapping
        for (int d = 0; d < mDistricts.getValue().length; d++)
            districtToIndex.put(mDistricts.getValue()[d], d);
    }

    public int getIndexByDistrictName(String district) {
        return districtToIndex.get(district);
    }
}
