package com.tardis.ordersamos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.HashMap;


public class ClassicMenuView extends ActionBarActivity {

    private Restaurant selectedRestaurant = null;
    private String previousActivity = "Stores";
    private SliderLayout mDemoSlider;
    private HashMap<String, Integer> finalPics;
    private ImageView ivCallFromClassicMenuView;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_menu_view);

        //// Initializing  selected Restaurant
        selectedRestaurant = Methods.getSelectedRestaurant(getApplicationContext());

        //// Changing Title to Restaurant name
        getSupportActionBar().setTitle(selectedRestaurant.getName());

        //// Determine which is the Previous Activity and try to animate
        if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
            try {
                previousActivity = getIntent().getStringExtra("PreviousActivity");
            } catch (Exception e) {
                Log.e("ERROR", "PREVIOUS ACTIVITY NOT FOUND");
            } finally {
                if (previousActivity.equals("Stores")) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            }
        }

        ////Initializing phone call ImageView
        ivCallFromClassicMenuView = (ImageView) findViewById(R.id.ivCallFromOldCatalogue);

        //// Initializing the photo slider
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        //// Creating HashMaps for every store
        HashMap<String, Integer> alati = new HashMap<String, Integer>();
        alati.put("0", R.drawable.alati_1);
        alati.put("1", R.drawable.alati2_converted);
        alati.put("2", R.drawable.alati3_converted);
        alati.put("3", R.drawable.alati4_converted);

        HashMap<String, Integer> evris = new HashMap<String, Integer>();
        evris.put("0", R.drawable.evris1_converted);
        evris.put("1", R.drawable.evris2_converted);
        evris.put("2", R.drawable.evris3_converted);
        evris.put("3", R.drawable.evris4_converted);
        evris.put("4", R.drawable.evris5_converted);

        HashMap<String, Integer> deli = new HashMap<String, Integer>();
        deli.put("0", R.drawable.deli1_converted);
        deli.put("1", R.drawable.deli2_converted);


        HashMap<String, Integer> fame = new HashMap<String, Integer>();
        fame.put("0", R.drawable.fame1_converted);
        fame.put("1", R.drawable.fame2_converted);
        fame.put("2", R.drawable.fame3_converted);
        fame.put("3", R.drawable.fame4_converted);
        fame.put("4", R.drawable.fame5_converted);
        fame.put("4", R.drawable.fame6_converted);


        HashMap<String, Integer> giro = new HashMap<String, Integer>();
        giro.put("0", R.drawable.giro1_converted);
        giro.put("1", R.drawable.giro2_converted);
        giro.put("2", R.drawable.giro3_converted);
        giro.put("3", R.drawable.giro4_converted);

        HashMap<String, Integer> koutala = new HashMap<String, Integer>();
        koutala.put("0", R.drawable.koutala1_converted);
        koutala.put("1", R.drawable.koutala2_converted);
        koutala.put("2", R.drawable.koutala3_converted);
        koutala.put("3", R.drawable.koutala4_converted);
        koutala.put("4", R.drawable.koutala5_converted);
/*      //// Old Kouzina
        HashMap<String, Integer> kouzina = new HashMap<String, Integer>();
        kouzina.put("0", R.drawable.kouzina1_converted);
        kouzina.put("1", R.drawable.kouzina2_converted);
        kouzina.put("2", R.drawable.kouzina3_converted);*/

        HashMap<String, Integer> kouzina = new HashMap<String, Integer>();
        kouzina.put("0", R.drawable.kouzina1);
        kouzina.put("1", R.drawable.kouzina2);
        kouzina.put("2", R.drawable.kouzina3);

        HashMap<String, Integer> megaro = new HashMap<String, Integer>();
        megaro.put("0", R.drawable.megaro1_converted);
        megaro.put("1", R.drawable.megaro2_converted);
        megaro.put("2", R.drawable.megaro3_converted);
        megaro.put("3", R.drawable.megaro4_converted);
        megaro.put("4", R.drawable.megaro5_converted);

        HashMap<String, Integer> mpousolas = new HashMap<String, Integer>();
        mpousolas.put("0", R.drawable.mpu1_converted);
        mpousolas.put("1", R.drawable.mpu2_converted);
        mpousolas.put("2", R.drawable.mpu3_converted);
        mpousolas.put("3", R.drawable.mpu4_converted);
        mpousolas.put("4", R.drawable.mpu5_converted);

        HashMap<String, Integer> nostos = new HashMap<String, Integer>();
        nostos.put("0", R.drawable.nostos1_converted);
        nostos.put("1", R.drawable.nostos2_converted);
        nostos.put("2", R.drawable.nostos3_converted);
        nostos.put("3", R.drawable.nostos4_converted);
        nostos.put("4", R.drawable.nostos5_converted);
        nostos.put("5", R.drawable.nostos6_converted);

        HashMap<String, Integer> periptero = new HashMap<String, Integer>();
        periptero.put("0", R.drawable.periptero1_converted);
        periptero.put("1", R.drawable.periptero2_converted);

        HashMap<String, Integer> plaisir = new HashMap<String, Integer>();
        plaisir.put("0", R.drawable.plaisir_converted_converted);

        HashMap<String, Integer> tzamas = new HashMap<String, Integer>();
        tzamas.put("0", R.drawable.tzamas1_converted);
        tzamas.put("1", R.drawable.tzamas2_converted);

        HashMap<String, Integer> vakxos = new HashMap<String, Integer>();
        vakxos.put("0", R.drawable.vakxos1_converted);
        vakxos.put("1", R.drawable.vakxos2_converted);
        vakxos.put("2", R.drawable.vakxos3_converted);
        vakxos.put("3", R.drawable.vakxos4_converted);

        HashMap<String, Integer> mania = new HashMap<String, Integer>();
        mania.put("0", R.drawable.mania1_converted);
        mania.put("1", R.drawable.mania2_converted);
        mania.put("2", R.drawable.mania3_converted);

        //// Determine which hashMap to use based on selected restaurant
        if (selectedRestaurant.getTrueName().equals("SaltPepper"))
            finalPics = alati;
        else if (selectedRestaurant.getTrueName().equals("Evrys"))
            finalPics = evris;
        else if (selectedRestaurant.getTrueName().equals("Vakxos"))
            finalPics = vakxos;
        else if (selectedRestaurant.getTrueName().equals("Kouzina"))
            finalPics = kouzina;
        else if (selectedRestaurant.getTrueName().equals("Fame"))
            finalPics = fame;
        else if (selectedRestaurant.getTrueName().equals("Giro"))
            finalPics = giro;
        else if (selectedRestaurant.getTrueName().equals("Megaro"))
            finalPics = megaro;
        else if (selectedRestaurant.getTrueName().equals("Psitomania"))
            finalPics = mania;
        else if (selectedRestaurant.getTrueName().equals("Nostos"))
            finalPics = nostos;
        else if (selectedRestaurant.getTrueName().equals("Periptero"))
            finalPics = periptero;
        else if (selectedRestaurant.getTrueName().equals("Tzamas"))
            finalPics = tzamas;
        else if (selectedRestaurant.getTrueName().equals("Deli"))
            finalPics = deli;
        else if (selectedRestaurant.getTrueName().equals("Plaisir"))
            finalPics = plaisir;
        else if (selectedRestaurant.getTrueName().equals("Koutala"))
            finalPics = koutala;
        else if (selectedRestaurant.getTrueName().equals("Mpousolas"))
            finalPics = mpousolas;
        else
            finalPics = evris;

        //// Adding hashMap info to the layout
        for (String name : finalPics.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            //// Initialize a SliderLayout
            textSliderView

                    .image(finalPics.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.stopAutoCycle();
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());

        //// Set OnClickListener for ivCallFromClassicMenuView
        ivCallFromClassicMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                int value = Preferences.loadPrefsInt(selectedRestaurant.getName() + "_ORDERS", -1,
                        getApplicationContext());
                Preferences.savePrefsInt(selectedRestaurant.getName() + "_ORDERS", value + 1, getApplicationContext());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.info_menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
                break;
            case R.id.store_info:
                Intent openRestaurantInfo = new Intent("com.tardis.ordersamos.RestaurantInformation");
                openRestaurantInfo.putExtra("PreviousActivity", "MenuView");
                startActivity(openRestaurantInfo
                );
                if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", getApplicationContext()).equals("ON")) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

    //// Method that controls a call to a restaurant
    public void callRestaurant(Context context, Context baseContext) {

        if (Preferences.loadPrefsString("DETECT_CARRIER", "YES", this).equals("YES")) {
            phoneNumber = Methods.getCarrierPhoneNumber(context, baseContext);
            if (phoneNumber == null) {
                Toast.makeText(context, "Το κατάστημα δεν διαθέτει τηλέφωνο " + Methods.getCarrierName(context), Toast.LENGTH_LONG).show();
                createCallDialog(selectedRestaurant.getPhoneNumbersInAnStringArray(), context);

            } else {
                if (Preferences.loadPrefsString("CALL_MODE", "PHONE", context).equals("PHONE")) {
                    Methods.phoneCallAutoCall(phoneNumber, getApplicationContext());
                } else {
                    Methods.phoneCall(phoneNumber, context);
                }
            }
        } else {
            createCallDialog(selectedRestaurant.getPhoneNumbersInAnStringArray(), context);
        }
        ivCallFromClassicMenuView.setVisibility(View.VISIBLE);
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
        ivCallFromClassicMenuView.setVisibility(View.VISIBLE);
    }
}
