package com.example.map_test1.view;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.map_test1.R;
import com.example.map_test1.custom.ChartAxisValueFormatter;
import com.example.map_test1.custom.XYMarkerView;
import com.example.map_test1.viewModel.SharedViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class ChartFragment extends Fragment{

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;
    private SharedViewModel mSharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v =  inflater.inflate(R.layout.fragment_chart, container, false);

        // initializing variable for bar chart.
        barChart = v.findViewById(R.id.idBarChart);

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mSharedViewModel.getCurrentCrimes().observe(getViewLifecycleOwner(), booleans -> updateChart());
        mSharedViewModel.getCurrentYear().observe(getViewLifecycleOwner(), integer -> updateChart());

        return v;
    }

    private void updateChart() {
        barChart.invalidate();
        // creating a new bar data set.
        barDataSet = new BarDataSet(getBarEntries(), "Criminal Data in " + mSharedViewModel.getCurrentYear().getValue() + " in " + mSharedViewModel.getCurrentDistrict().getValue());

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        barChart.setBackgroundColor(0xFFEEEAEE);

        /* MY CHANGES START */
        IAxisValueFormatter xAxisFormatter = new ChartAxisValueFormatter(barChart, mSharedViewModel);
        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart
    }




    private ArrayList<BarEntry> getBarEntries() {
        Log.d("chartTest", "getBarEntries()");
        // creating a new array list
        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        int pos = 0;
        int d = mSharedViewModel.getIndexByDistrictName(mSharedViewModel.getCurrentDistrict().getValue());
        int y = mSharedViewModel.getCurrentYear().getValue() - mSharedViewModel.getYears().getValue()[0];
        Integer[] records = mSharedViewModel.getRecords().getValue()[d][y];
        for (int c = 0; c < mSharedViewModel.getCrimes().getValue().length; c++) {
            if (records[c] != 0 && mSharedViewModel.getCurrentCrimes().getValue()[c])
                barEntriesArrayList.add(new BarEntry((float)pos++, records[c]));
        }
        return barEntriesArrayList;
    }
}