package com.example.map_test1.view;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.map_test1.R;
import com.example.map_test1.model.Utils;
import com.example.map_test1.custom.ChartAxisValueFormatter;
import com.example.map_test1.custom.XYMarkerView;
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

public class ChartFragment extends Fragment implements OnChartValueSelectedListener {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ChartFragment", "oncrtview");
        View v =  inflater.inflate(R.layout.fragment_chart, container, false);

        super.onCreate(savedInstanceState);

        // initializing variable for bar chart.
        barChart = v.findViewById(R.id.idBarChart);

        // creating a new bar data set.
        barDataSet = new BarDataSet(getBarEntries(), "Criminal Data in " + Utils.getCurrentYear() + " in " + Utils.getCurrentDistrict());

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

        /* MY CHANGES START */
        barChart.setOnChartValueSelectedListener(this);

        IAxisValueFormatter xAxisFormatter = new ChartAxisValueFormatter(barChart);
        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setTag("ChartFragment");
        Log.d("info", "view created, tag = " + view.getTag());

        ImageView refresh = view.findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(btn ->  {
            Log.d("info", "btn clicked");
            Navigation.findNavController(view).navigate(R.id.action_chartFragment_self);
        });
    }

    private final RectF onValueSelectedRectF = new RectF();
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = onValueSelectedRectF;
        barChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = barChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + barChart.getLowestVisibleX() + ", high: "
                        + barChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }

    private ArrayList<BarEntry> getBarEntries() {
        // creating a new array list
        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        int pos = 0;
        int d = Utils.getCurrentDistrictIndex();
        int y = Utils.getCurrentYearIndex();
        int[] data = Utils.getData()[d][y];
        for (int c = 0; c < Utils.getCrimes().length; c++) {
            if (data[c] != 0 && Utils.getCurrentCrimes()[c])
                barEntriesArrayList.add(new BarEntry((float)pos++, data[c]));
        }
        return barEntriesArrayList;
    }
}