package com.tardis.ordersamos;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.XLabels;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.ArrayList;
import java.util.List;


public class Chart extends ActionBarActivity implements OnChartValueSelectedListener,SeekBar.OnSeekBarChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        BarChart mChart = (BarChart) findViewById(R.id.chart);




        // if more than 60 entries are displayed in the chart, no values will be added
        mChart.setMaxVisibleValueCount(60);

        // disable 3D
        mChart.set3DEnabled(false);
        mChart.setValueTextColor(Color.WHITE);
        mChart.setGridColor(Color.WHITE);
        mChart.setDrawingCacheBackgroundColor(Color.WHITE);
        mChart.setBorderColor(Color.WHITE);
        mChart.setDrawBarShadow(false);
        mChart.setDescription(" ");
        mChart.setDrawVerticalGrid(false);
        mChart.setDrawHorizontalGrid(false);
        mChart.setDrawGridBackground(false);

        XLabels xLabels = mChart.getXLabels();
        xLabels.setPosition(XLabels.XLabelPosition.BOTTOM);
        xLabels.setCenterXLabelText(true);
        xLabels.setSpaceBetweenLabels(0);
        xLabels.setTextColor(Color.WHITE);
        xLabels.isAdjustXLabelsEnabled();

        mChart.setDrawYLabels(false);
        mChart.setDrawLegend(false);
        mChart.fitScreen();



        List<String> rests = Methods.getRestaurantNames(getApplicationContext());

        // setting data
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i <rests.size(); i++) {

            int val1 = Preferences.loadPrefsInt(rests.get(i) + "_ORDERS",0,
                    getApplicationContext());
            yVals1.add(new BarEntry((int) val1, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < rests.size(); i++) {
            if(rests.get(i).equals("Η κουζίνα της μαμάς μου")){
                xVals.add("Κουζ");
            }else if(rests.get(i).equals("Η κουτάλα του μάγειρα")){
                xVals.add("Κουτ");
            }else if(rests.get(i).equals("Delivery Τζαμάς")){
                xVals.add("Τζαμ");
            }else {
                xVals.add(rests.get(i).substring(0, 4));
            }
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();

        // add a nice and smooth animation
        mChart.animateY(2500);
    }

    @Override
    public void onValueSelected(Entry entry, int i) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
