package com.tardis.ordersamos;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Utilities.Preferences;

import me.alexrs.wavedrawable.WaveDrawable;


public class Information extends ActionBarActivity {

    private ImageView ivInformationLogo, ivGraph, ivMail;
    private int timesClicked = 0;
    private TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //// Getting reference to ImageViews
        ivInformationLogo = (ImageView) findViewById(R.id.ivInformationLogo);
        ivGraph = (ImageView) findViewById(R.id.ivGraph);
        ivMail = (ImageView) findViewById(R.id.ivMail);
        tvVersionName = (TextView) findViewById(R.id.tvVersion);
        final String versionName = BuildConfig.VERSION_NAME;

        //// Setting Version Name
        tvVersionName.setText("Version : "+versionName);

        //// Animation on LogoImageView
        final WaveDrawable waveDrawable;
        waveDrawable = new WaveDrawable(Color.parseColor("#ff642f"), 500);
        ivInformationLogo.setBackgroundDrawable(waveDrawable);
        waveDrawable.setWaveInterpolator(new BounceInterpolator());
        waveDrawable.startAnimation();

        //// Set action performed when LogoImageView is clicked
        ivInformationLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timesClicked == 15) {
                    timesClicked = 0;
                    startActivity(new Intent("com.tardis.ordersamos.EasterEgg"));
                } else {
                    timesClicked++;
                }

            }
        });

        //// Set action performed when Mail is clicked
        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// If animations are enabled
                if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {

                    //// Start the animation and then open mailing app
                    new ScaleOutAnimation(v).setDuration(190).setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                            //  startActivity(new Intent("com.tardis.ordersamos.Random"));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "order.at.samos@gmail.com"));
                            intent.putExtra(Intent.EXTRA_TEXT, "Sent From Order At Samos App");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order At Samos App User");
                            startActivity(intent);
                        }
                    }).animate();

                } else {

                    //// Open the mailing app
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "order.at.samos@gmail.com"));
                    intent.putExtra(Intent.EXTRA_TEXT, "Sent From Order At Samos App");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order At Samos App User");
                    startActivity(intent);
                }
            }
        });

        //// Sets on click Listener to ivGraph
        ivGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// If animations are enabled when the animation ends open the Graph activity
                if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                new ScaleOutAnimation(v).setDuration(190).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        startActivity(new Intent("com.tardis.ordersamos.Chart"));
                    }
                }).animate();

                    ivGraph.setVisibility(View.VISIBLE);
            } else {
                    //// Open it immediately
                startActivity(new Intent("com.tardis.ordersamos.Chart"));
            }
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

        //// Set iv Visibility when resumed due to animation nature
        ivMail.setVisibility(View.VISIBLE);
        ivGraph.setVisibility(View.VISIBLE);
    }
}
