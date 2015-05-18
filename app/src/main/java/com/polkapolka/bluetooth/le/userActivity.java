package com.polkapolka.bluetooth.le;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class userActivity extends ActionBarActivity {



     public static int activityCounterBed = 0;
     public static int activityCounterDesk = 0;
     public static int activityCounterWalking=0;
     public static int activityCounterPatient=0;
     public static int activityCounterMachine=0;
     public static int activityCounterOther=0;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        String[] activityOptionsUser = {getString(R.string.ActivityOptionsUserBed), getString(R.string.ActivityOptionsUserDesk),
                getString(R.string.ActivityOptionsUserWalking), getString(R.string.ActivityOptionsUserpatient), getString(R.string.ActivityOptionsUserMachine), getString(R.string.ActivityOptionsUserOther)};

// The ListAdapter acts as a bridge between the data and each ListItem
        // You fill the ListView with a ListAdapter. You pass it a context represented by
        // this. A Context provides access to resources you need.
        // android.R.layout.simple_list_item_1 is one of the resources needed.
        // It is a predefined layout provided by Android that stands in as a default
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityOptionsUser);
        // listview display data in a scrollable list
        ListView theListView = (ListView) findViewById(R.id.theListView);
        // tell the ListView what data to use
        theListView.setAdapter(theAdapter);



        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String activityOptionsUser = getString(R.string.YouSelectedToast) + String.valueOf(adapterView.getItemAtPosition(position));
                Toast.makeText(userActivity.this, activityOptionsUser, Toast.LENGTH_SHORT).show();

                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Standing next to a bed") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Aan het bed werken")) {
                    activityCounterBed++;
                    System.out.print("@string/ActivityOptionsUserBed");
                    System.out.println(activityCounterBed);

                }
                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Working at a desk") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Aan een bureau werken")) {
                    activityCounterDesk++;
                    System.out.print("@string/ActivityOptionsUserDesk");
                    System.out.println(activityCounterDesk);

                }
                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Walking") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Lopen")) {
                    activityCounterWalking++;
                    System.out.print("@string/ActivityOptionsUserWalking");
                    System.out.println(activityCounterWalking);
                }
                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Handling a patient") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Patient begeleiden/tillen")) {
                    activityCounterPatient++;
                    System.out.print("@string/ActivityOptionsUserPatient");
                    System.out.println(activityCounterPatient);
                }
                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Operating machine") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Bezig met de bediening van apparatuur")) {
                    activityCounterMachine++;
                    System.out.print("@string/ActivityOptionsUsermachine");
                    System.out.println(activityCounterMachine);
                }
                if (String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Other") || String.valueOf(adapterView.getItemAtPosition(position)).equalsIgnoreCase("Overige")) {
                    activityCounterOther++;
                    System.out.print("@string/ActivityOptionsUserOther");
                    System.out.println(activityCounterOther);
                }
                Intent GoBackToHome = new Intent(userActivity.this,DeviceControlActivity.class);
                startActivity(GoBackToHome);

                //setResult(RESULT_OK);
                //finish();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_useractivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, DeviceControlActivity.class);
                startActivityForResult(intent, 0);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}