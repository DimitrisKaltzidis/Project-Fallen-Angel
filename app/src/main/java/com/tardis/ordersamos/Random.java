package com.tardis.ordersamos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.RotationAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Logic.Structure.RandomFood;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;
import com.tardis.ordersamos.Utilities.WaveAnimation;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Random extends ActionBarActivity {

    private TextView tvRandomStore, tvRandomFood;
    private Button bRandomRefresh, bRandomDialer, bInfo;
    private CardView cvRandom;
    private WaveAnimation waveDrawable;
    private RandomFood rf;
    private ArrayList<Restaurant> allRestaurants;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        allRestaurants = Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext());

        //// Initialize TextViews and Buttons
        cvRandom = (CardView) findViewById(R.id.card_viewRI3);
        bRandomRefresh = (Button) findViewById(R.id.ivRandomRefresh);
        bRandomDialer = (Button) findViewById(R.id.ivRandomDialer);
        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setVisibility(View.GONE);
        tvRandomStore = (TextView) findViewById(R.id.tvRandomStore);
        tvRandomFood = (TextView) findViewById(R.id.tvRandomFood);

        //// Creating WaveAnimation
        waveDrawable = new WaveAnimation(Color.parseColor("#ff642f"), 650, 500);

        //// On clickListener on Random Refresh
        bRandomRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// If dialer icon is gone due to an animation make it visible again
                try {
                    bRandomDialer.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }

                //// If an animation is running on the background stop it
                try {
                    waveDrawable.stopAnimation();
                } catch (Exception e) {

                }

                //// Set text to zero if something is wrong
                try {
                    tvRandomFood.setText("");
                    tvRandomStore.setText("");
                } catch (Exception e) {
                    Log.d("CRASH", "TEXT SETTING CLEARING");
                }

                //// Get a random Food
                rf = Methods.getRandomFood(allRestaurants,
                        getApplicationContext());

                //// Animate the refresh button
                new RotationAnimation(v)
                        .setDuration(200)
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {


                            }
                        })
                        .animate();

                //// If the random food description is empty then set visibility to info button GONE
                if (rf.getFoodDescription().equals("-")) {
                    bInfo.setVisibility(View.GONE);
                } else {
                    bInfo.setVisibility(View.VISIBLE);
                }

                //// Set text to textViews from the randomFood
                try {
                    tvRandomStore.setText(rf.getRestaurantName() + " - "
                            + rf.getFoodCategory());
                    tvRandomFood.setText(rf.getFoodNameAndPrice());
                    LinearLayout llcard = (LinearLayout) findViewById(R.id.llFirstCardViewRI3);
                    llcard.setBackgroundDrawable(waveDrawable);

                    //// Animate the cardView background based on preferences
                    if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                        waveDrawable.startAnimation();
                    }
                } catch (Exception e) {

                    //// If something is gone wrong Log me and and set text to '~'
                    Log.d("CRASH", "TEXT SETTING ");
                    tvRandomStore.setText("~");
                    tvRandomFood.setText("~");
                }


            }
        });

        //// Shows an alert dialog on screen with the food description
        bInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogOnScreen(rf.getFoodDescription(), "Περιγραφή");
            }
        });


        //// Calls a restaurant
        bRandomDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// Animates the button and calls a restaurant based on preferences
                if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                    new ScaleOutAnimation(v).setDuration(190).setListener(new AnimationListener() {


                        @Override
                        public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                            callRestaurant(getApplicationContext(), getBaseContext());
                        }

                    }).animate();
                } else {
                    callRestaurant(getApplicationContext(), getBaseContext());
                }

                //// Change preferences order number
                int value = Preferences.loadPrefsInt(rf.getRestaurantName() + "_ORDERS", -1,
                        getApplicationContext());
                Preferences.savePrefsInt(rf.getRestaurantName() + "_ORDERS", value + 1, getApplicationContext());
            }
        });


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
    protected void onResume() {
        super.onResume();
        //// ReInitialize the allRestaurant Variable
        allRestaurants = Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext());
    }

    //// Shows an alert dialog on screen
    private void ShowAlertDialogOnScreen(String dialog, String title) {

        new SweetAlertDialog(this)
                .setTitleText(title)
                .setContentText(dialog)
                .show();
    }

    //// Method that controls a call to a restaurant
    public void callRestaurant(Context context, Context baseContext) {
        if (Preferences.loadPrefsString("DETECT_CARRIER", "YES", this).equals("YES")) {
            phoneNumber = Methods.getCarrierPhoneNumber(context, baseContext);
            if (phoneNumber == null) {
                Toast.makeText(context, "Το κατάστημα δεν διαθέτει τηλέφωνο " + Methods.getCarrierName(context), Toast.LENGTH_LONG).show();
                createCallDialog(rf.getRestaurant().getPhoneNumbersInAnStringArray(), context);

            } else {
                if (Preferences.loadPrefsString("CALL_MODE", "PHONE", context).equals("PHONE")) {
                    Methods.phoneCallAutoCall(phoneNumber, getApplicationContext());
                } else {
                    Methods.phoneCall(phoneNumber, context);
                }
            }
        } else {
            createCallDialog(rf.getRestaurant().getPhoneNumbersInAnStringArray(), context);
        }
        bRandomDialer.setVisibility(View.VISIBLE);
    }

    //// Creates a CallDialog with Phones for the User to select
    public void createCallDialog(String[] phones, final Context context) {
        final String[] s = phones;
        new AlertDialog.Builder(this).setTitle("Τηλέφωνα:")
                .setItems(s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String ari8mos[] = s[which].split(":");
                        ari8mos[1].replace(" ", "");

                        if (Preferences.loadPrefsString("CALL_MODE", "PHONE", context).equals("PHONE")) {
                            Methods.phoneCallAutoCall(ari8mos[1], context);
                        } else {
                            Methods.phoneCall(ari8mos[1], context);
                        }
                    }
                }).show();
        bRandomDialer.setVisibility(View.VISIBLE);
    }
}
