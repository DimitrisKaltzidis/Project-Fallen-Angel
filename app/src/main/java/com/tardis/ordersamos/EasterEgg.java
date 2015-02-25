package com.tardis.ordersamos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tardis.ordersamos.EasterEggRelated.FlakeView;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.Timer;
import java.util.TimerTask;


public class EasterEgg extends ActionBarActivity {

    private FlakeView flakeView;
    private int timePressed = 1;
    private RelativeLayout container;
    private MediaPlayer mp = null, mpM = null;
    private Context context;
    private Button bBanKouzina, bUnbanKouzina;
    private LinearLayout llEEControls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        ////DEN PISTEUW NA THELEIS KAI EDW SXOLIA?????
        //// PLAKA KANW EVALA :p
        context = this;

        //// Media player initialization
        mp = MediaPlayer.create(this, R.raw.rick_roll);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);  // for repeat song
        mp.start();

        //// Change song
        mpM = MediaPlayer.create(context, R.raw.mel);
        mpM.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mpM.setLooping(true);  // for repeat song

        //// Finding Views
        llEEControls = (LinearLayout) findViewById(R.id.llEEControls);
        container = (RelativeLayout) findViewById(R.id.container);

        //// Adding Flake view to activity container
        flakeView = new FlakeView(this);
        container.addView(flakeView);

        //// Den xerw ti kanei?!?!?!?!
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        //// Setting background based on android Version
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            container.setBackgroundDrawable(getResources().getDrawable(R.drawable.paralax));
        } else {
            container.setBackground(getResources().getDrawable(R.drawable.paralax));
        }

        //// Ban and Unban Kouzina
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        bBanKouzina = (Button) findViewById(R.id.bBanKouzina);
        bBanKouzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.banRestaurant("Κουζίνα", getApplicationContext(), Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext()));

            }
        });

        bUnbanKouzina = (Button) findViewById(R.id.bUnBanKouzina);
        bUnbanKouzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.unBanRestaurant("Κουζίνα", getApplicationContext(), Methods.getRestaurantsAndTheirBanStateAndTheirMenu(getApplicationContext()));
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        //// More or less Buttons control
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        final Button more = (Button) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flakeView.addFlakes(flakeView.getNumFlakes());
            }
        });
        final Button less = (Button) findViewById(R.id.less);
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flakeView.subtractFlakes(flakeView.getNumFlakes() / 2);
            }
        });
        CheckBox accelerated = (CheckBox) findViewById(R.id.accelerated);
        accelerated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flakeView.setLayerType(isChecked ? View.LAYER_TYPE_NONE : View.LAYER_TYPE_SOFTWARE,
                        null);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        //// Hidden Button clicked
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        final Button bMyEasterEgg = (Button) findViewById(R.id.bMel);
        bMyEasterEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (timePressed == 20) {

                    bMyEasterEgg.setClickable(false);
                    //// Make controls invisible
                    llEEControls.setVisibility(View.GONE);

                    //// Remove burgers and add hearts
                    flakeView.changeIcon(0);
                    flakeView.addFlakes(1);
                    flakeView.subtractFlakes(flakeView.getNumFlakes() - 1);

                    //// Stop previous song
                    try {
                        mp.release();
                    } catch (Exception e) {

                    }


                    mpM.start();

                    //// Change Background and Action Bar text
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        container.setBackgroundDrawable(getResources().getDrawable(R.drawable.mee_paralax));
                    } else {
                        container.setBackground(getResources().getDrawable(R.drawable.mee_paralax));
                    }
                    getSupportActionBar().setTitle(getResources().getString(R.string.my_easter_egg));

                    //// Timer to add Flakes
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            flakeView.addFlakes(250);
                        }
                    }, 190 /*amount of time in milliseconds before execution*/);

                    //// Reset timesPressed variable
                    timePressed = 1;
                }
                timePressed++;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
        try {
            mp.release();
            mpM.release();

        } catch (Exception e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
