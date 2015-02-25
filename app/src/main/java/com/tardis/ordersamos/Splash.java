package com.tardis.ordersamos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.data.BarEntry;
import com.tardis.ordersamos.Logic.Structure.Place;
import com.tardis.ordersamos.Utilities.GMailSender;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Splash extends ActionBarActivity {

    ImageView ivSplashLogo;
    int processors = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //// ActionBar shit
        getSupportActionBar().hide();

        //// Logo Image size
        ivSplashLogo = (ImageView) findViewById(R.id.ivSplashLogo);
        ivSplashLogo.getLayoutParams().height = Methods
                .calculateComponentSize(300, 500, getResources());
        ivSplashLogo.getLayoutParams().width = Methods
                .calculateComponentSize(300, 500, getResources());

        //// Get processors number
        try {
            processors = Runtime.getRuntime().availableProcessors();
            Log.e("NUMBER OF CORES", processors + "");
        } catch (Exception e) {
            Log.e("ERROR", "DETECTING NUMBER OF CORES");
        }


        //// Preference setup

        if (Preferences.loadPrefsString("FIRST_TIME_A", "YES", getApplicationContext()).equals(
                "YES")) {

            //// Changes FIRST_TIME preference to NO
            Preferences.savePrefsString("FIRST_TIME_A", "NO",
                    getApplicationContext());



            //// Setting Preferences according to processors
            if (processors >= 4) {
                Preferences.savePrefsString("WINDOWS_TRANSITIONS", "ON",
                        getApplicationContext());
                Preferences.savePrefsString("ANIMATIONS", "ON",
                        getApplicationContext());
            } else {
                Preferences.savePrefsString("WINDOWS_TRANSITIONS", "OFF",
                        getApplicationContext());
                Preferences.savePrefsString("ANIMATIONS", "OFF",
                        getApplicationContext());
            }

            //// Saving Unbanned Restaurants name for the first Time in Preferences
            try {
                Preferences
                        .writeList(
                                getApplicationContext(),
                                Methods.getRestaurantNames(getApplicationContext()),
                                "restnames");
            } catch (Exception e) {
                Log.e("ERROR", "SAVING UNBANNED RESTAURANT NAMES TO PREFERENCES FOR THE FIRST TIME");
            }

            //// Saving catalogue MODE and Gesture Recognition State to Preferences
            Preferences.savePrefsString("MENU_STYLE", "NEW", getApplicationContext());
            Preferences.savePrefsString("GESTURE_DETECTION", "OFF", getApplicationContext());


        }

        //// Every month at 5
        Time now = new Time();
        now.setToNow();

        //// Report user statistics
        if (now.monthDay >= 5 && now.monthDay <= 10) {
            Preferences.savePrefsString("REPORT_STATISTICS", "YES", getApplicationContext());
        }

        //// Report user statistics
        if (Preferences.loadPrefsString("REPORT_STATISTICS", "YES", getApplicationContext()).equals("YES")) {
            new LongOperation().execute("");
        }

        //// Report user statistics
        if (now.monthDay > 10) {
            Preferences.savePrefsString("SUBMIT_REPORT", "YES", getApplicationContext());
        }


        //// Save Order numbers to preferences for the first time
        Preferences.saveListIntValuesToPreferencesWithPrefix(getApplicationContext(), Methods.getRestaurantNames(getApplicationContext()), "_ORDERS");

        //// Timer to open MainMenu
        new Timer().schedule(new TimerTask() {
            public void run() {
                startActivity(new Intent(Splash.this, MainMenu.class));
            }
        }, 2500 /*amount of time in milliseconds before execution*/);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

    ////AsyncTask for mail sending
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            if (Preferences.loadPrefsString("SUBMIT_REPORT", "YES", getApplicationContext()).equals("YES")) {
                Preferences.savePrefsString("SUBMIT_REPORT", "NO", getApplicationContext());

                String mailBody = getMailBody(getApplicationContext());
                String mailSubject = getMailSubject();

                try {
                    GMailSender sender = new GMailSender("order.at.samos@gmail.com", "kalaeimai?");
                    sender.sendMail(mailSubject,
                            mailBody,
                            "order.at.samos@gmail.com",
                            "order.at.samos@gmail.com");

                    System.out.println("MAIL SENT");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }

            }
            Preferences.savePrefsString("REPORT_STATISTICS", "NO", getApplicationContext());
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    //// Creates the email body
    private String getMailBody(Context context) {
        List<String> rests = Methods.getRestaurantNames(context);

        // setting data
        ArrayList<String> yVals1 = new ArrayList<String>();

        String mailBody = "";

        int maxPosition = 0;
        int max = 0;
        for (int i = 0; i < rests.size(); i++) {

            int val1 = Preferences.loadPrefsInt(rests.get(i) + "_ORDERS", 0,
                    getApplicationContext());
            if (max < val1) {
                max = val1;
                maxPosition = i;
            }

            yVals1.add(">" + rests.get(i) + "_ORDERS = " + val1);
            System.out.println(">" + rests.get(i) + "_ORDERS = " + val1 + "<");
            mailBody = mailBody + ">" + rests.get(i) + "_ORDERS = " + val1 + "<" + "\n";
        }

        mailBody = "\n" + mailBody + "\n" + " MORE_ORDERS_FROM>>> " + rests.get(maxPosition) + "\n";

        mailBody = "\n" + mailBody + "\n" + " PHONE>>> " + Methods.getPhoneManufacturerAndModel();

        mailBody = "\n" + mailBody + "\n" + " SCREEN_DPI>>> " + Methods.getScreenDensity(getResources());

        mailBody = "\n" + mailBody + "\n" + " CARRIER>>> " + Methods.getCarrierName(context);

        mailBody = "\n" + mailBody + "\n" + " CPU_CORES>>> " + processors;

        return mailBody;

    }

    //// Create mail subject
    private String getMailSubject() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String mailSubject = "USER_REPORT_" + formattedDate;

        return mailSubject;
    }

}



