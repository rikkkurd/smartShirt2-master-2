package com.polkapolka.bluetooth.le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by s104419 on 5/12/2015.
 */
public class ArduinoReceiver extends BroadcastReceiver {
    public final static ArduinoHandler arduinoHandler = new ArduinoHandler();

    @Override
    public void onReceive(Context context, Intent intent) {

        arduinoHandler.context = context;
        final String action = intent.getAction();
        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//            mConnected = true;
//            updateConnectionState(R.string.connected);
//            invalidateOptionsMenu();
            // do nothing
        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//            mConnected = false;
//            updateConnectionState(R.string.disconnected);
//            invalidateOptionsMenu();
//            clearUI();
            // do nothing
        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            // Show all the supported services and characteristics on the user interface.
            //  displayGattServices(mBluetoothLeService.getSupportedGattServices());
            // do nothing
        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


            // guard: check if the bytes are available
            if (!intent.hasExtra(BluetoothLeService.EXTRA_DATA)) {
                return;
            }
            // call handler
            try {
                arduinoHandler.handleArduinoBytes(intent);
                System.out.println("called arduino handler");
            } catch (Exception e) {
                System.out.println("Skipped incoming data, because of exception!");
                e.printStackTrace();
            }


        }
    }
}
