package com.tardis.ordersamos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.Timer;
import java.util.TimerTask;


public class RestaurantInformation extends ActionBarActivity {

    private String previousActivty = "MenuView";
    private TextView tvRestaurantName, tvRestaurantCategory, tvRestaurantAddress, tvRestaurantDaysOpen, tvRestaurantHoursOpen, tvRestaurantAllPhones;
    private ImageView ivRestaurantLogo, ivFacebook;
    private Restaurant selectedRestaurant = null;
    private WebView facebookWebView;
    private CardView cardViewFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_information);

        //// Enable Up button on ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //// Get Previous Activity name to determine On Up Button Clicked action
        try {
            previousActivty = getIntent().getStringExtra("PreviousActivity");
        } catch (Exception e) {
            Log.d("ERROR_PREVIOUS_ACTIVITY", "NO PREVIOUS ACTIVITY");
        }

        //// Getting Reference to xml View Components
        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvRestaurantCategory = (TextView) findViewById(R.id.tvRestaurantCategory);
        tvRestaurantAddress = (TextView) findViewById(R.id.tvRestaurantAddress);
        tvRestaurantDaysOpen = (TextView) findViewById(R.id.tvRestaurantDaysOpen);
        tvRestaurantHoursOpen = (TextView) findViewById(R.id.tvRestaurantHoursOpen);
        tvRestaurantAllPhones = (TextView) findViewById(R.id.tvRestaurantAllPhones);
        ivRestaurantLogo = (ImageView) findViewById(R.id.ivImageLogo);
        ivFacebook = (ImageView) findViewById(R.id.ivFacebook);

        //// Getting Reference to FacebookWebView
        facebookWebView = (WebView) findViewById(R.id.wvFacebook);

        ////
        cardViewFacebook = (CardView) findViewById(R.id.card_viewRI3);

        //// Enable Javascript for webView
        WebSettings webSettings = facebookWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        facebookWebView.setWebViewClient(new WebViewClient());

        //// Get selected restaurant
        selectedRestaurant = Methods.getSelectedRestaurant(getApplicationContext());

        //// Setting information to textViews and imageViews
        ivRestaurantLogo.setBackgroundResource(getResources().getIdentifier(selectedRestaurant.getImage(), "drawable",
                getPackageName()));
        ivRestaurantLogo.setImageResource(R.drawable.ri_trans);

        //// If no data exist for the restaurant then remove the unused views
        if (selectedRestaurant.getImage().equals("-"))
            ivRestaurantLogo.setVisibility(View.GONE);

        if (selectedRestaurant.getFacebook().equals("-")) {
            ivFacebook.setVisibility(View.GONE);
            facebookWebView.setVisibility(View.GONE);
            cardViewFacebook.setVisibility(View.GONE);
        } else {
            if (isNetworkAvailable()) {
               /* Toast.makeText(getApplicationContext(), "Φόρτωση σελίδας facebook",
                        Toast.LENGTH_SHORT).show();*/
                /*SuperCardToast.create(this, "Φόρτωση σελίδας facebook", SuperToast.Duration.SHORT).show();*/
                final SuperCardToast superCardToast = new SuperCardToast(this, SuperToast.Type.PROGRESS_HORIZONTAL);
                superCardToast.setText("Φόρτωση σελίδας facebook");
                superCardToast.setIndeterminate(false);
                superCardToast.setProgressIndeterminate(true);
                superCardToast.setBackground(SuperToast.Background.WHITE);
                superCardToast.setTextColor(getResources().getColor(R.color.Red_soft));
                superCardToast.setTextSize(19);
                superCardToast.setDuration(SuperToast.Duration.MEDIUM);
                superCardToast.show();


                ivFacebook.setVisibility(View.GONE);
                facebookWebView.setVisibility(View.VISIBLE);
                //// Setting url to webView if connected to the internet
                facebookWebView.loadUrl(selectedRestaurant.getFacebook()
                );
            } else {
                ivFacebook.setVisibility(View.VISIBLE);
                facebookWebView.setVisibility(View.GONE);
            }

        }

        //// Setting text views mainly
        tvRestaurantName.setText(selectedRestaurant.getName());
        tvRestaurantCategory.setText(selectedRestaurant.getDescription());
        tvRestaurantAddress.setText(selectedRestaurant.getAddress());
        tvRestaurantHoursOpen.setText(selectedRestaurant.getOpeningHour() + "-"
                + selectedRestaurant.getClosingHour());

        //// If CloseDay equals none set standard text
        if (selectedRestaurant.getCloseDay().equals("none")) {
            tvRestaurantDaysOpen.setText("Ανοιχτά κάθε μέρα");
        } else {
            tvRestaurantDaysOpen.setText("Κλειστά " + selectedRestaurant.getCloseDay());
        }


        String allPhones = "";
        if (!selectedRestaurant.getPhone().equals("-")) {
            allPhones = allPhones + "Σταθερό  : " + selectedRestaurant.getPhone() + "\n"
                    + "\n";
        }
        if (!selectedRestaurant.getPhoneCosmote().equals("-")) {
            allPhones = allPhones + "Cosmote : " + selectedRestaurant.getPhoneCosmote() + "\n"
                    + "\n";
        }

        if (!selectedRestaurant.getPhoneVodafone().equals("-")) {
            allPhones = allPhones + "Vodafone: " + selectedRestaurant.getPhoneVodafone()
                    + "\n" + "\n";
        }

        if (!selectedRestaurant.getPhoneWind().equals("-")) {
            allPhones = allPhones + "Wind        : " + selectedRestaurant.getPhoneWind()
                    + "\n" + "\n";
        }

        tvRestaurantAllPhones.setText(allPhones);

        //// Setting OnClickListener to ivFacebook
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                    new ScaleOutAnimation(v).setDuration(190).setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse(selectedRestaurant.getFacebook())));
                        }
                    }).animate();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //// Open menu activity depending previous activity variable
            case android.R.id.home:

                Intent openPreviousActivity = null;
                if (previousActivty.equals("MenuView")) {
                    openPreviousActivity = new Intent("com.tardis.ordersamos.MenuView");
                    openPreviousActivity.putExtra("PreviousActivity", "RestaurantInfo");
                } else {
                    openPreviousActivity = new Intent("com.tardis.ordersamos.ClassicMenuView");
                    openPreviousActivity.putExtra("PreviousActivity", "RestaurantInfo");
                }
                NavUtils.navigateUpTo(this, openPreviousActivity);
                finish();
                if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
                break;


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ivFacebook.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
