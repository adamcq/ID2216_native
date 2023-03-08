package com.example.map_test1.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.map_test1.repository.CriminalDataRepository;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SharedViewModel extends ViewModel {

    // static data variables
    private MutableLiveData<Integer[]> mYears;
    private MutableLiveData<String[]> mDistricts;
    private MutableLiveData<String[]> mCrimes;
    private MutableLiveData<Integer[][][]> mRecords;
    CriminalDataRepository mRepo;


    // dynamic state variables
    private final MutableLiveData<Integer> mCurrentYear = new MutableLiveData<>(2022);
    private final MutableLiveData<String> mCurrentDistrict = new MutableLiveData<>(null);
    private final MutableLiveData<boolean[]> mCurrentCrimes = new MutableLiveData<>( new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true});
    private final MutableLiveData<Integer> mCurrentMaxCrimeCount = new MutableLiveData<>(null);
    private final MutableLiveData<Integer[]> mCurrentCrimeCounts = new MutableLiveData<>(null);
    private final Map<String, Integer> districtToIndex = new HashMap<>();
    private final MutableLiveData<GeoJsonLayer> mLayer = new MutableLiveData<>(null);

    // init ViewModel
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
    private void initMappings() {
        // create district to index mapping
        for (int d = 0; d < mDistricts.getValue().length; d++)
            districtToIndex.put(mDistricts.getValue()[d], d);
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

    public void setLayer(GeoJsonLayer layer) {
        Log.d("SVM info", "layer set " + mLayer.getValue());
        mLayer.setValue(layer);
    }


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

    public int findDistrictIndexByName(String district) {
        return districtToIndex.get(district);
    }
}
