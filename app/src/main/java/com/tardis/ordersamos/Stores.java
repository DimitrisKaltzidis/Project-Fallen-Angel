package com.tardis.ordersamos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.BlindAnimation;
import com.easyandroidanimations.library.FoldAnimation;
import com.easyandroidanimations.library.FoldLayout;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.easyandroidanimations.library.SlideOutAnimation;
import com.easyandroidanimations.library.SlideOutUnderneathAnimation;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.tardis.ordersamos.Logic.Structure.Place;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.ArrayList;


public class Stores extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {

    private int verticalThickness = 100;
    private LinearLayout llStores;
    private View temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        //// Calculate button height
        verticalThickness = Methods.calculateComponentSize(222, 600, getResources());

        //// Getting Place Item tha contains the restaurants to display
        Place Karlovasi = Methods.getThePlace(getApplicationContext());

        //// Get reference to LinearLayout
        llStores = (LinearLayout) findViewById(R.id.llStores);

        //// Filling the LinearLayout with Restaurants KOUZINA FIRST Remove Methods Call to get back to normal
        AddRestaurantsCardView(llStores, Methods.changeRestaurantPosition(Karlovasi.getUnbannedRestaurants(), "Kouzina", 0));


        //// For the tutorial
        final RelativeLayout rlHelp = (RelativeLayout) findViewById(R.id.rlTut);
        Button bSettingsFromRLHelp = (Button) findViewById(R.id.bOpenSettingsMenu);

        if (Preferences.loadPrefsString("FIRST_TIME", "YES", getApplicationContext()).equals("NO")) {
            rlHelp.setVisibility(View.GONE);
        }

        rlHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.savePrefsString("FIRST_TIME", "NO",
                        getApplicationContext());
                rlHelp.setVisibility(View.GONE);
            }
        });

        bSettingsFromRLHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.savePrefsString("FIRST_TIME", "NO",
                        getApplicationContext());
                startActivity(new Intent("com.tardis.ordersamos.Settings"));
            }
        });

    }

    //// Adds Restaurants to the LinearLayout
    public void AddRestaurantsCardView(LinearLayout llObjects,
                                       ArrayList<Restaurant> lista) {
        for (int i = 0; i < lista.size(); i++) {
            CardView cvRestaurant = new CardView(this);
            cvRestaurant.setCardElevation((float) 15.0);
            cvRestaurant.setRadius((float) 15.0);
            TextView tvRestaurantName = new TextView(this);
            cvRestaurant.setId(i);
            String s = lista.get(i).getName();
            tvRestaurantName.setText(s);
            tvRestaurantName.setTypeface(null, Typeface.BOLD);
            tvRestaurantName.setGravity(Gravity.CENTER);
            tvRestaurantName.setTextColor(Color.parseColor("#4f5d73"));
            cvRestaurant.setOnClickListener(this);
            if (!s.equals("Κουζίνα")) {
                cvRestaurant.setOnLongClickListener(this);
            }
            tvRestaurantName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            tvRestaurantName.setHeight(verticalThickness);
            cvRestaurant.addView(tvRestaurantName);
            llObjects.addView(cvRestaurant);
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //// Change the cardView dimensions based on screen DPI
            if (Methods.getScreenDensity(getResources()) < 410) {
                cardLayoutParams.setMargins(12, 10, 12, 10);
            } else {
                cardLayoutParams.setMargins(12, 18, 12, 18);
            }
            cvRestaurant.setLayoutParams(cardLayoutParams);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
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
    public void onClick(final View v) {

        //// Saves the restaurant position to Preferences
        Preferences.savePrefsInt("SELECTED_RESTAURANT", v.getId(), getApplicationContext());

        ////Animate the Opening of a menuView activity based on Preferences
        if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
            new SlideOutAnimation(v).setDuration(190).setListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                    if (Preferences.loadPrefsString("MENU_STYLE", "NEW", getApplicationContext()).equals("NEW")) {
                        Intent openNewMenuView = new Intent("com.tardis.ordersamos.MenuView");
                        openNewMenuView.putExtra("PreviousActivity", "Stores");
                        startActivity(openNewMenuView);
                    } else {
                        Intent openNewMenuView = new Intent("com.tardis.ordersamos.ClassicMenuView");
                        openNewMenuView.putExtra("PreviousActivity", "Stores");
                        startActivity(openNewMenuView);
                    }
                    v.setVisibility(View.INVISIBLE);
                }
            }).setDirection(Animation.DIRECTION_RIGHT).animate();
        } else {
            if (Preferences.loadPrefsString("MENU_STYLE", "NEW", getApplicationContext()).equals("NEW")) {
                Intent openNewMenuView = new Intent("com.tardis.ordersamos.MenuView");
                openNewMenuView.putExtra("PreviousActivity", "Stores");
                startActivity(openNewMenuView);
            } else {
                Intent openNewMenuView = new Intent("com.tardis.ordersamos.ClassicMenuView");
                openNewMenuView.putExtra("PreviousActivity", "Stores");
                startActivity(openNewMenuView);
            }
        }

    }

    @Override
    protected void onRestart() {

        //// reload the activity
        super.onRestart();
        Intent i = getIntent();
        startActivity(i);
        finish();
        if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }


    }

    //// Removes a store from the displayed restaurants
    @Override
    public boolean onLongClick(final View v) {

        //// Initializing temp variable
        temp = v;

        //// Set clickState
        v.setClickable(false);

        //// Animate an exit Button
        new ScaleOutAnimation(/*findViewById(R.id.llStores)*/v).setDuration(250).setListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                //// Getting Place Item tha contains the restaurants to display
                Place Karlovasi = Methods.getThePlace(getApplicationContext());

                ArrayList<Restaurant> restaurantst = Methods.changeRestaurantPosition(Karlovasi.getUnbannedRestaurants(), "Kouzina", 0);

                Methods.banRestaurant(restaurantst.get(v.getId()).getName(), getApplicationContext(), restaurantst);

                llStores.removeAllViews();

                AddRestaurantsCardView(llStores, Methods.changeRestaurantPosition(Karlovasi.getUnbannedRestaurants(), "Kouzina", 0));
            }
        }).animate();

        return false;
    }
}
