package com.polkapolka.bluetooth.le;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


public class userOverview extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_overview);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        int activityCounterBed = userActivity.activityCounterBed;
        int activityCounterDesk = userActivity.activityCounterDesk;
        int activityCounterWalking = userActivity.activityCounterWalking;
        int activityCounterPatient = userActivity.activityCounterPatient;
        int activityCounterMachine = userActivity.activityCounterMachine;
        int activityCounterOther = userActivity.activityCounterOther;

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, activityCounterBed),
                new DataPoint(2, activityCounterDesk),
                new DataPoint(3, activityCounterWalking),
                new DataPoint(4, activityCounterPatient),
                new DataPoint(5, activityCounterMachine),
                new DataPoint(6, activityCounterOther),
                new DataPoint(6.8, 0)
//                new DataPoint(1, 1),
//                new DataPoint(2, 2),
//                new DataPoint(3, 3),
//                new DataPoint(4, 1),
//                new DataPoint(5, 1),
//                new DataPoint(6, 2),
//                new DataPoint(6.8, 0)

        });

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(userOverview.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(userOverview.this, deskInfo.class);
                startActivity(intent);
            }
        });


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((((int) data.getX() * 255 / 6)), ((int) Math.abs(data.getX() * 1)), 100);
            }
        });

//colors of code below are okay
//        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//            @Override
//            public int get(DataPoint data) {
//                return Color.rgb((int) data.getX() * 255 / 6, (int) Math.abs(data.getX() * 255 / 4), 100);
//            }
//        });
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{getString(R.string.Bed), getString(R.string.Desk), getString(R.string.Walking), getString(R.string.Patient), getString(R.string.Machine), getString(R.string.Other), ""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series.setSpacing(25);
        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.BLACK);
graph.addSeries(series);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DeviceControlActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
