package com.polkapolka.bluetooth.le;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
//import java.util.Arrays;

/**
 * Created by s104419 on 4/2/2015.
 */
public class ArduinoHandler {

    /**
     * Contants
     */
    private final static int INVALID_CALIBRATION_VALUE = 1000;
    private final static int SENSOR_ONE_PACKET_ID = 0;
    private final static int SENSOR_TWO_PACKET_ID = 1;
    private final static int BAD_PITCH1_THRESHOLD = 20;
    private final static int BAD_PITCH2_THRESHOLD = 20;
    private final static int BAD_ROLL1_THRESHOLD = 20;
    private final static int BAD_ROLL2_THRESHOLD = 20;
    private final static int BAD_PITCH1_TIME = 3000;
    private final static int BAD_PITCH2_TIME = 3000;
    private final static int BAD_ROLL1_TIME = 3000;
    private final static int BAD_ROLL2_TIME = 3000;

    /**
     * Calibration values
     */
    public boolean isCalibrating = false;
    private final ArrayList<Integer> roll1Calibration = new ArrayList<Integer>();
    private final ArrayList<Integer> roll2Calibration = new ArrayList<Integer>();
    private final ArrayList<Integer> pitch2Calibration = new ArrayList<Integer>();
    private final ArrayList<Integer> pitch1Calibration = new ArrayList<Integer>();

    /**
     * Object references
     */

    /**
     * Pitch listener
     */
    private long lastBadPitch1Time = 0;
    private long lastBadRoll1Time = 0;
    private long lastBadPitch2Time = 0;
    private long lastBadRoll2Time = 0;
    private boolean NotificationShownRoll1 = false;
    private boolean NotificationShownPitch1 = false;
    private boolean NotificationShownRoll2 = false;
    private boolean NotificationShownPitch2 = false;


    /**
     * Sensor variables
     */
    private int roll1 = 0;
    private int heading1 = 0;
    private int pitch1 = 0;
    private int roll2 = 0;
    private int heading2 = 0;
    private int pitch2 = 0;
    public Context context;

    /**
     * Constructor
     */
    public ArduinoHandler() {

        // variables

    }

    /**
     * Handle bytes from the Arduino.
     *
     * @param intent
     */
    public void handleArduinoBytes(Intent intent) {

        // guard: check if the bytes are available
        if (!intent.hasExtra(BluetoothLeService.EXTRA_DATA)) {
            return;
        }

        // variables
        String dataString = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
        String[] dataStringArr = dataString.split(",");
        int[] intArr = new int[dataStringArr.length];

        // loop all data pieces
        for (int i = 0; i < dataStringArr.length; i++) {
            intArr[i] = Integer.parseInt(dataStringArr[i]);
        }

        // System.out.println(Arrays.toString(intArr));

        // guard: check if not null
        if (intArr == null) {
            return;
        }

        // guard: check if lenght is valid
        if (intArr.length != 4) {
            return;
        }

        // get the packet id
        int packetId = intArr[0];

        // switch on the packet id
        switch (packetId) {
            case SENSOR_ONE_PACKET_ID:

                roll1 = intArr[1];
                pitch1 = intArr[2];
                heading1 = intArr[3];

                if (isCalibrating) {
                    roll1Calibration.add(roll1);
                    roll2Calibration.add(roll2);
                    pitch1Calibration.add(pitch1);
                    pitch2Calibration.add(pitch2);

                    System.out.println("calculate average");
                }

                System.out.println("[DATA SENSOR ONE]: " + roll1 + ", " + pitch1 + ", " + heading1);
                break;

            case SENSOR_TWO_PACKET_ID:
                roll2 = intArr[1];
                pitch2 = intArr[2];
                heading2 = intArr[3];
                System.out.println("[DATA SENSOR TWO]: " + roll2 + ", " + pitch2 + ", " + heading2);
                break;
        }
        // check the pitch posture
        checkPitch1Posture();
        checkRoll1Posture();
        checkPitch2Posture();
        checkRoll2Posture();

    }

    /**
     * Check the pitch posture
     */
    private void checkPitch1Posture() {

        // variables
        //int deltaPitch = pitch1 - pitch2;
        int deltaPitch1 =  calculateDelta(getCalibratedPitch1(), pitch1);
        //  deltaPitch = (deltaPitch < 0) ? -deltaPitch : deltaPitch;
        //int deltaPitch = pitch1 - pitch2;
        System.out.print("deltaPitch1");
        System.out.println(deltaPitch1);

        // guard: check if the threshold is not exceeded //if so reset the timer to 0 and create no notification then return
        if (deltaPitch1 < BAD_PITCH1_THRESHOLD) {
            lastBadPitch1Time = 0;
            NotificationShownPitch1 = false;
            return;
        }

        // variables
        long currentMillis1 = System.currentTimeMillis();

        // guard: check if the timer is already set,
        if (lastBadPitch1Time <= 0) {
            System.out.println("Set last bad pitch1 time!");
            lastBadPitch1Time = currentMillis1;
            return;
        }

        // check if the timer is exceeded (the user is in a bad posture for longer than BAD_PITCH_TIME and the user needs a warning also check if not other notifications are active
        if (currentMillis1 - lastBadPitch1Time > BAD_PITCH1_TIME && !NotificationShownRoll1 && !NotificationShownPitch1&& !NotificationShownRoll2 && !NotificationShownPitch2) {

            // show notification
            System.out.println("Notification shown Pitch1!");
            DeviceControlActivity.showNotificationInMenu(context);
            NotificationShownPitch1 = true;
        }
    }
    private void checkPitch2Posture() {

        // variables
        //int deltaPitch = pitch1 - pitch2;
        int deltaPitch2 =  calculateDelta(getCalibratedPitch2(), pitch2);
        //  deltaPitch = (deltaPitch < 0) ? -deltaPitch : deltaPitch;
        //int deltaPitch = pitch1 - pitch2;
        System.out.print("deltaPitch2");
        System.out.println(deltaPitch2);

        // guard: check if the threshold is exceeded //|| (deltaPitch >= -BAD_PITCH_THRESHOLD && <= 0;))
        if (deltaPitch2 < BAD_PITCH2_THRESHOLD) {
            lastBadPitch2Time = 0;
            NotificationShownPitch2 = false;
            return;
        }

        // variables
        long currentMillis2 = System.currentTimeMillis();

        // guard: check if the timer is already set
        if (lastBadPitch2Time <= 0) {
            System.out.println("Set last bad pitch2 time!");
            lastBadPitch2Time = currentMillis2;
            return;
        }

        // check if the timer is exceeded and the user needs a warning + guard that no notification is already shown
        if (currentMillis2 - lastBadPitch2Time > BAD_PITCH2_TIME && !NotificationShownRoll1 && !NotificationShownPitch2&& !NotificationShownRoll2 && !NotificationShownPitch2) {

            // show notification
            System.out.println("Notification shown Pitch2!");
            DeviceControlActivity.showNotificationInMenu(context);
            NotificationShownPitch2 = true;
        }
    }
    private void checkRoll1Posture() {

        // variables
        //int deltaPitch = pitch1 - pitch2;
        int deltaRoll1 = calculateDelta(getCalibratedRoll1(), roll1);
        //  deltaPitch = (deltaPitch < 0) ? -deltaPitch : deltaPitch;
        //int deltaPitch = pitch1 - pitch2;
        System.out.print("deltaRoll1:::");
        System.out.println(deltaRoll1);

        // guard: check if the threshold is exceeded //|| (deltaPitch >= -BAD_PITCH_THRESHOLD && <= 0;))
        if (deltaRoll1 < BAD_ROLL1_THRESHOLD) {
            lastBadRoll1Time = 0;
            NotificationShownRoll1 = false;
            return;
        }

        // variables
        long currentMillis3 = System.currentTimeMillis();

        // guard: check if the timer is already set
        if (lastBadRoll1Time <= 0) {
            System.out.println("Set last bad Roll1 time!");
            lastBadRoll1Time = currentMillis3;
            return;
        }

        // check if the timer is exceeded and the user needs a warning
        if (currentMillis3 - lastBadRoll1Time > BAD_ROLL1_TIME && !NotificationShownRoll2 && !NotificationShownPitch2&& !NotificationShownRoll1 && !NotificationShownPitch1) {

            // show notification
            System.out.println("Notification shown Roll1!");
            DeviceControlActivity.showNotificationInMenu(context);
            NotificationShownRoll1 = true;
        }
    }


    private void checkRoll2Posture() {

        // variables
        //int deltaPitch = pitch1 - pitch2;
        int deltaRoll2 = calculateDelta(getCalibratedRoll2(), roll2);
        //  deltaPitch = (deltaPitch < 0) ? -deltaPitch : deltaPitch;
        //int deltaPitch = pitch1 - pitch2;
        System.out.print("deltaRoll2:::");
        System.out.println(deltaRoll2);

        // guard: check if the threshold is exceeded //|| (deltaPitch >= -BAD_PITCH_THRESHOLD && <= 0;))
        if (deltaRoll2 < BAD_ROLL1_THRESHOLD) {
            lastBadRoll2Time = 0;
            NotificationShownRoll2 = false;
            return;
        }

        // variables
        long currentMillis4 = System.currentTimeMillis();

        // guard: check if the timer is already set
        if (lastBadRoll2Time <= 0) {
            System.out.println("Set last bad Roll1 time!");
            lastBadRoll2Time = currentMillis4;
            return;
        }

        // check if the timer is exceeded and the user needs a warning
        if (currentMillis4 - lastBadRoll2Time > BAD_ROLL2_TIME && !NotificationShownRoll2 && !NotificationShownPitch2&& !NotificationShownRoll1 && !NotificationShownPitch1) {

            // show notification
            System.out.println("Notification shown Roll1!");
            DeviceControlActivity.showNotificationInMenu(context);
            NotificationShownRoll2 = true;
        }
    }

    //calculate calibrated values from the values stored in the array while calibrating
    public int getCalibratedRoll1() {
        return calculateAverage(roll1Calibration);
    }

    public int getCalibratedRoll2() {
        return calculateAverage(roll2Calibration);
    }

    public int getCalibratedPitch1() {
        return calculateAverage(pitch1Calibration);
    }

    public int getCalibratedPitch2() {
        return calculateAverage(pitch2Calibration);
    }


    // function to calculate average value of the arraylist, guard to check if the list is empty, if so calibration value returns INVALID_CALIBRATION_VALUE
    public int calculateAverage(ArrayList<Integer> list) {

        // guard: check for empty list
        if (list.size() <= 0) {
            return INVALID_CALIBRATION_VALUE;
        }

        // variables
        int total = 0;

        for (int roll : list) {
            total += roll;
        }
        System.out.println("calculate average");
        return total / list.size();
    }
// function to calculate the difference of the measured value and the calibrated value,
// if the arraylist is empty the calibrated value = INVALID and the difference will be 0 to prevent any
// notifications before the calibration was started
    public int calculateDelta(int calibrationValue, int currentValue) {

        // guard: check if the calibration value is valid
        if (calibrationValue == INVALID_CALIBRATION_VALUE) {
            return 0;
        }

        return Math.abs(calibrationValue - currentValue);
    }

}

