package com.example.readmap2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
//Justin Liang
//baja sae 2/14/22

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling functions
        readGPSData();
        createGraph();
    }
    //creating data hashmap
    private LinkedHashMap<Double, Double> GPSData = new LinkedHashMap<Double, Double>();

    private void readGPSData() {
        InputStream is = getResources().openRawResource(R.raw.et_break_data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
            try{
                //Step over headers
                reader.readLine();
                while((line = reader.readLine()) != null) {
                    //Split by ','
                    String[] tokens= line.split(",");
                    //Read the Data, assigning GPSSample
                    GPSSample sample = new GPSSample();
                    //Reading over the -999 values
                    if (Double.parseDouble(tokens[0]) != -999) {
                        sample.setLatitude(Double.parseDouble(tokens[0]));
                        sample.setLongitude(Double.parseDouble(tokens[1]));
                        GPSData.put(sample.getLatitude(),sample.getLongitude());
                        //Log.d("MyActivity","Just created: " + sample) ;
                    }
                }
            } catch (IOException e) {
                Log.wtf("My Activity", "Error reading datafile on line" + line, e);
                e.printStackTrace();
            }
    }

    private void createGraph() {
        GraphView graphView;
        graphView = findViewById(R.id.idGraphView);
        //initializing dataset
        DataPoint[] data = new DataPoint[GPSData.size()];
        //putting data into numerical order
        Map<Double, Double> orderedMap = new TreeMap<>(GPSData);
        int i = 0;
        for(Map.Entry<Double, Double> entry :orderedMap.entrySet()) {
            data[i] = new DataPoint(entry.getKey(), entry.getValue());
            i+= 1; }
        //can play with these to alter graph. Check out main xml as well
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(data);
        graphView.setTitle("My Graph View");
        graphView.setTitleColor(R.color.purple_200);
        graphView.setTitleTextSize(18);
        graphView.addSeries(series);
        //https://www.geeksforgeeks.org/line-graph-view-in-android-with-example/

    }


}