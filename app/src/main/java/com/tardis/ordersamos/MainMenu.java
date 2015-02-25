package com.tardis.ordersamos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Utilities.Preferences;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainMenu extends ActionBarActivity implements View.OnClickListener {

    //// MainMenu Buttons
    private Button bStores, bSearch, bBanList, bRandom, bSettings, bInformation;

    //// MainMenu TextViews
    private TextView tvSearch, tvStores, tvBanList, tvRandom, tvInfo, tvSettings;
    private int animationDuration = 190;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //// ActionBar shit
        getSupportActionBar().hide();

        //// Getting Button Reference
        bStores = (Button) findViewById(R.id.bStore);
        bSearch = (Button) findViewById(R.id.bSearch);
        bBanList = (Button) findViewById(R.id.bBan);
        bRandom = (Button) findViewById(R.id.bRandom);
        bSettings = (Button) findViewById(R.id.bSettings);
        bInformation = (Button) findViewById(R.id.bInfo);

        //// Settings OnClickListener to Buttons
        bStores.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bBanList.setOnClickListener(this);
        bRandom.setOnClickListener(this);
        bSettings.setOnClickListener(this);
        bInformation.setOnClickListener(this);

        //// Getting TextView Reference
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvStores = (TextView) findViewById(R.id.tvStores);
        tvBanList = (TextView) findViewById(R.id.tvBanList);
        tvRandom = (TextView) findViewById(R.id.tvRandom);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvSettings = (TextView) findViewById(R.id.tvSettings);

        //// Display changes if first time
        final int versionCode = BuildConfig.VERSION_CODE;
        final String versionName = BuildConfig.VERSION_NAME;
        final Context context = this;


        if (Preferences.loadPrefsInt("VERSION_CODE", -1, getApplicationContext()) == -1) {

            Preferences.savePrefsInt("VERSION_CODE", versionCode, getApplicationContext());
            ShowAlertDialogFirstTime(versionName);
        }

        //// Changed Version
        if ((versionCode > Preferences.loadPrefsInt("VERSION_CODE", -1, getApplicationContext()))) {
            Preferences.savePrefsInt("VERSION_CODE", versionCode, getApplicationContext());
            ShowAlertDialogOnScreen("Bug fixes\nΚαταγραφή στατιστικών", "Έκδοση " + versionName);
        }

    }

    //// Display alert dialog for the first time
    private void ShowAlertDialogFirstTime(final String VersionName) {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.star)
                .setTitleText("Γειά σου")
                .setContentText("Σε ευχαριστούμε που κατέβασες αυτήν την εφαρμογή. Αν σου αρέσει βαθμολόγησε μας με 5 αστέρια στο play store!")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        ShowAlertDialogOnScreen("Bug fixes\nΚαταγραφή στατιστικών\n", "Έκδοση " + VersionName);

                    }
                })
                .show();
    }

    /// Display an alert dialog
    private void ShowAlertDialogOnScreen(String dialog, String title) {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setCustomImage(R.drawable.news)
                .setContentText(dialog)
                .setConfirmText("OK")
                .show();
    }


    @Override
    public void onClick(final View v) {

        //// View for use in inner class and Intent for Open next activity
        Intent openActivityIntent = null;

        //// Animate Button if ANIMATIONS preference is enabled
        if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {


            if (v.getId() == R.id.bStore) {
                new ScaleOutAnimation(findViewById(R.id.tvStores)).setDuration(animationDuration).animate();
            } else if (v.getId() == R.id.bSearch) {
                new ScaleOutAnimation(findViewById(R.id.tvSearch)).setDuration(animationDuration).animate();
            } else if (v.getId() == R.id.bBan) {
                new ScaleOutAnimation(findViewById(R.id.tvBanList)).setDuration(animationDuration).animate();
            } else if (v.getId() == R.id.bRandom) {
                new ScaleOutAnimation(findViewById(R.id.tvRandom)).setDuration(animationDuration).animate();
            } else if (v.getId() == R.id.bSettings) {
                new ScaleOutAnimation(findViewById(R.id.tvSettings)).setDuration(animationDuration).animate();
            } else if (v.getId() == R.id.bInfo) {
                new ScaleOutAnimation(findViewById(R.id.tvInfo)).setDuration(animationDuration).animate();
            }
            new ScaleOutAnimation(v).setDuration(animationDuration).setListener(new AnimationListener() {


                @Override
                public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                    Intent openActivityIntent = null;
                    if (v.getId() == R.id.bStore) {
                        new ScaleOutAnimation(findViewById(R.id.tvStores)).setDuration(animationDuration).animate();
                        openActivityIntent = new Intent("com.tardis.ordersamos.Stores");
                        openActivityIntent.putExtra("PreviousActivity", "MainMenu");
                    } else if (v.getId() == R.id.bSearch) {
                        openActivityIntent = new Intent("com.tardis.ordersamos.Search");
                    } else if (v.getId() == R.id.bBan) {
                        openActivityIntent = new Intent("com.tardis.ordersamos.BanList");
                    } else if (v.getId() == R.id.bRandom) {
                        openActivityIntent = new Intent("com.tardis.ordersamos.Random");
                    } else if (v.getId() == R.id.bSettings) {
                        openActivityIntent = new Intent("com.tardis.ordersamos.Settings");
                    } else if (v.getId() == R.id.bInfo) {
                        openActivityIntent = new Intent("com.tardis.ordersamos.Information");
                    }

                    startActivity(openActivityIntent);
                    if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON"))
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }

            }).animate();

        } else {/// If ANIMATIONS preference is disabled
            if (v.getId() == R.id.bStore) {
                openActivityIntent = new Intent("com.tardis.ordersamos.Stores");
                openActivityIntent.putExtra("PreviousActivity", "MainMenu");
            } else if (v.getId() == R.id.bSearch) {
                openActivityIntent = new Intent("com.tardis.ordersamos.Search");
            } else if (v.getId() == R.id.bBan) {
                openActivityIntent = new Intent("com.tardis.ordersamos.BanList");
            } else if (v.getId() == R.id.bRandom) {
                openActivityIntent = new Intent("com.tardis.ordersamos.Random");
            } else if (v.getId() == R.id.bSettings) {
                openActivityIntent = new Intent("com.tardis.ordersamos.Settings");
            } else if (v.getId() == R.id.bInfo) {
                openActivityIntent = new Intent("com.tardis.ordersamos.Information");
            }
            startActivity(openActivityIntent);
            if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON"))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //// Make all views visible
        bStores.setVisibility(View.VISIBLE);
        bSearch.setVisibility(View.VISIBLE);
        bBanList.setVisibility(View.VISIBLE);
        bRandom.setVisibility(View.VISIBLE);
        bSettings.setVisibility(View.VISIBLE);
        bInformation.setVisibility(View.VISIBLE);
        tvBanList.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);
        tvRandom.setVisibility(View.VISIBLE);
        tvSearch.setVisibility(View.VISIBLE);
        tvSettings.setVisibility(View.VISIBLE);
        tvStores.setVisibility(View.VISIBLE);

    }
}
