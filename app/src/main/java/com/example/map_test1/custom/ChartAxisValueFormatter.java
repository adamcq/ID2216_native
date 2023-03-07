package com.example.map_test1.custom;

import com.example.map_test1.viewModel.SharedViewModel;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by philipp on 02/06/16.
 */
public class ChartAxisValueFormatter implements IAxisValueFormatter
{
    private final BarLineChartBase<?> chart;
    private SharedViewModel mSharedViewModel;

    public ChartAxisValueFormatter(BarLineChartBase<?> chart, SharedViewModel sharedViewModel) {
        this.chart = chart;
        mSharedViewModel = sharedViewModel;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Object[] nonZeroCrimes = getNonZeroCrimes(); // district, year
        return (String) nonZeroCrimes[(int)value];
    }

    private Object[] getNonZeroCrimes() {
        int d = mSharedViewModel.getIndexByDistrictName(mSharedViewModel.getCurrentDistrict().getValue());
        int y =  mSharedViewModel.getCurrentYear().getValue() - mSharedViewModel.getYears().getValue()[0];
        Integer[][][] records = mSharedViewModel.getRecords().getValue();
        String[] crimes = mSharedViewModel.getCrimes().getValue();
        ArrayList<String> nonZeroCrimes = new ArrayList<>();
        for (int c = 0; c < crimes.length; c++) {
            if (records[d][y][c] != 0 && mSharedViewModel.getCurrentCrimes().getValue()[c]) {
                nonZeroCrimes.add(crimes[c]);
            }
        }
        return nonZeroCrimes.toArray();
    }
}
