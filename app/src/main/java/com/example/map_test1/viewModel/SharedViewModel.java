package com.example.map_test1.viewModel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.map_test1.repository.CriminalDataRepository;

public class SharedViewModel extends ViewModel {
    // only loaded once from database
    private MutableLiveData<Integer[]> mYears = new MutableLiveData<>();
    private MutableLiveData<String[]> mDistricts = new MutableLiveData<>();
    private MutableLiveData<String[]> mCrimes = new MutableLiveData<>();
    private MutableLiveData<Integer[][][]> mRecords = new MutableLiveData<>();


    // constantly updated by the user input - these variables reflect current state to be displayed
    private MutableLiveData<Integer> mCurrentYear = new MutableLiveData<>(2022); // TODO init with method
    private MutableLiveData<String> mCurrentDistrict = new MutableLiveData<>(null); // TODO init with click
    private MutableLiveData<boolean[]> mCurrentCrimes = new MutableLiveData<>( new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true});
    private MutableLiveData<Integer> mCurrentMaxCrimeCount = new MutableLiveData<>(null); // TODO init with method

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
        updateCurrentCrimes(new boolean[]{true, false});
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

    // setters
    public void updateCurrentYear(Integer currentYear) {
        mCurrentYear.setValue(currentYear);
    }

    public void updateCurrentDistrict(String currentDistrict) {
        mCurrentDistrict.setValue(currentDistrict);
    }

    public void updateCurrentCrimes(boolean[] currentCrimes) {
        mCurrentCrimes.setValue(currentCrimes);
    }

    public void updateCurrentMaxCrimeCount(Integer currentMaxCrimeCount) {
        mCurrentMaxCrimeCount.setValue(currentMaxCrimeCount);
    }
}
