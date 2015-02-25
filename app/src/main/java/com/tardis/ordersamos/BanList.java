package com.tardis.ordersamos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.BounceAnimation;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;
import com.tardis.ordersamos.Utilities.WaveAnimation;

import java.util.ArrayList;


public class BanList extends ActionBarActivity implements View.OnClickListener {

    private int verticalThickness = 100;
    private LinearLayout llRestaurants;
    private ArrayList<Restaurant> restaurants;
    private WaveAnimation waveAnimation;
    private View waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_list);


        ////Git tracking for branch local jim THIS IS A TEST LINE

        //// Calculate button height
        verticalThickness = Methods.calculateComponentSize(222, 600, getResources());

        ////  Getting reference to llRestaurants Ban List
        llRestaurants = (LinearLayout) findViewById(R.id.llStoresBanList);

        //// Getting reference to View
        waveView = (View) findViewById(R.id.vBanList);

        //// List of Restaurants
        restaurants = Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext());

        //// Fill the scrollView with Restaurants
        AddBanButtonsCardView(llRestaurants, restaurants);

    }


    public void AddBanButtonsCardView(LinearLayout BanLayout,
                                      ArrayList<Restaurant> lista) {
        for (int i = 0; i < lista.size(); i++) {
            CardView cvRestaurant = new CardView(this);
            cvRestaurant.setCardElevation((float) 18.0);
            cvRestaurant.setRadius((float) 15.0);
            TextView tvRestaurantName = new TextView(this);
            cvRestaurant.setId(i);
            if (lista.get(i).isBanned() == false) {
                cvRestaurant.setCardBackgroundColor(Color.parseColor("#ffffff"));
                tvRestaurantName.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                cvRestaurant.setCardBackgroundColor(Color.parseColor("#c75c5c"));
                tvRestaurantName.setBackgroundColor(Color.parseColor("#c75c5c"));
                cvRestaurant.setCardElevation((float) 5.0);
            }
            String s = lista.get(i).getName();
            tvRestaurantName.setText(s);
            tvRestaurantName.setTypeface(null, Typeface.BOLD);
            tvRestaurantName.setGravity(Gravity.CENTER);
            tvRestaurantName.setTextColor(Color.parseColor("#4f5d73"));
            cvRestaurant.setOnClickListener(this);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

                tvRestaurantName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
            // UNBANABLE KOUZINA MAMAS
            if (lista.get(i).getName().equals("Κουζίνα")) {
                cvRestaurant.setVisibility(View.GONE);
            }
            tvRestaurantName.setHeight(verticalThickness);
            cvRestaurant.addView(tvRestaurantName);
            BanLayout.addView(cvRestaurant);
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //// Set cardView margins depending on ban state
            if (lista.get(i).isBanned() == false) {
                if (Methods.getScreenDensity(getResources()) < 410) {
                    cardLayoutParams.setMargins(12, 10, 12, 10);
                } else {
                    cardLayoutParams.setMargins(12, 18, 12, 18);
                }
            } else {
                if (Methods.getScreenDensity(getResources()) < 410) {
                    cardLayoutParams.setMargins(18, 10, 18, 10);
                } else {
                    cardLayoutParams.setMargins(18, 18, 18, 18);
                }
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
    public void onClick(View v) {

        //// Ban or Unban a Restaurant
        if (restaurants.get(v.getId()).isBanned()) {
            restaurants = Methods.unBanRestaurant(restaurants.get(v.getId()).getName(), getApplicationContext(), restaurants);
            waveAnimation = new WaveAnimation(Color.parseColor("#ffffff"), 2000, 650);
        } else {
            restaurants = Methods.banRestaurant(restaurants.get(v.getId()).getName(), getApplicationContext(), restaurants);
            waveAnimation = new WaveAnimation(Color.parseColor("#c75c5c"), 2000, 650);
        }

        //// Animate A WaveAnimation and a BounceAnimation if ANIMATIONS are enabled
        if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {

            //// Animate the Button Clicked
            new BounceAnimation(v)
                    .setBounceDistance(10)
                    .setDuration(190).setListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //// Wave animation parameters on waveView
                    waveView.setBackgroundDrawable(waveAnimation);
                    waveAnimation.setWaveInterpolator(new AccelerateDecelerateInterpolator());
                    waveAnimation.startAnimation();

                    //// Remove all Views from ScrollView
                    llRestaurants.removeAllViews();

                    //// Add Views again
                    AddBanButtonsCardView(llRestaurants, restaurants);

                    //// Start Listening for clicks
                    ///setClickableRecursive(llRestaurants,true);

                }
            })
                    .animate();
        } else {

            //// Remove all Views from ScrollView
            llRestaurants.removeAllViews();

            //// Add Views again
            AddBanButtonsCardView(llRestaurants, restaurants);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (restaurants == null) {
            restaurants = Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext());
        }
    }

    //// Stop or Start Listening to click for a layout
    public static void setClickableRecursive(View view, boolean isClickable) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setClickableRecursive(group.getChildAt(i), isClickable);
            }
        } else {
            view.setClickable(isClickable);
        }
        System.out.println("LISTENING FOR CLICKS " + isClickable);
    }
}
