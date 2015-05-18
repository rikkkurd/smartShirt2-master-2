package com.polkapolka.bluetooth.le;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class helpingPatientInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping_patient_info);

        Button infoDesk = (Button) findViewById(R.id.infoDesk);
        infoDesk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(view.getContext(), deskInfo.class);
                                            startActivityForResult(intent, 0);
                                        }
                                    }


        );

        Button bodyPosture = (Button) findViewById(R.id.infoBodyPosture);
        bodyPosture.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view2) {
                                               Intent intent2 = new Intent(view2.getContext(), bodyPostureInfo.class);
                                               startActivityForResult(intent2, 0);
                                           }
                                       }


        );
        Button bedPosture = (Button) findViewById(R.id.infoBedPosture);
        bedPosture.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view3) {
                                              Intent intent3 = new Intent(view3.getContext(), bedInfo.class);
                                              startActivityForResult(intent3, 0);
                                          }
                                      }


        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_helping_patient_info, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.back_button_to_user_overview:
                Intent intent = new Intent(this, userOverview.class);
                startActivityForResult(intent, 0);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



}