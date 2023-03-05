package com.example.map_test1.custom;

import com.example.map_test1.model.Utils;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by philipp on 02/06/16.
 */
public class ChartAxisValueFormatter implements IAxisValueFormatter
{
    private final BarLineChartBase<?> chart;

    public ChartAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Object[] nonZeroCrimes = getNonZeroCrimes(); // district, year
        return (String) nonZeroCrimes[(int)value];
    }

    private Object[] getNonZeroCrimes() {
        int d = Utils.getCurrentDistrictIndex();
        int y = Utils.getCurrentYearIndex();
        ArrayList<String> nonZeroCrimes = new ArrayList<>();
        for (int i = 0; i < Utils.getCrimes().length; i++) {
            if (Utils.getData()[d][y][i] != 0 && Utils.getCurrentCrimes()[i]) {
                nonZeroCrimes.add(Utils.getCrimes()[i]);
            }
        }
        return nonZeroCrimes.toArray();
    }
}
