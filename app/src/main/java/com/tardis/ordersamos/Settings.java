package com.tardis.ordersamos;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.tardis.ordersamos.Utilities.Preferences;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Settings extends ActionBarActivity {

    private ToggleButton tbCarrierCall, tbAutoCall, tbStores, tbCategories,
            tbWindowTransitions, tbAnimations, tbSwipe, tbOldMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //// Initializing toggle Buttons
        tbCarrierCall = (ToggleButton) findViewById(R.id.tbSettingsCarrier);
        tbAutoCall = (ToggleButton) findViewById(R.id.tbSettingsAutoCall);
        tbStores = (ToggleButton) findViewById(R.id.tbSettingsRandomStores);
        tbCategories = (ToggleButton) findViewById(R.id.tbSettingsRandomCategories);
        tbWindowTransitions = (ToggleButton) findViewById(R.id.tbSettingsGraphicsWindowsTransitions);
        tbAnimations = (ToggleButton) findViewById(R.id.tbSettingsGraphicsAnimations);
        tbSwipe = (ToggleButton) findViewById(R.id.tbSettingsControlsSwipe);
        tbOldMenuView = (ToggleButton) findViewById(R.id.tbSettingsControlsOldMenuView);

        //// Setting state based on Preferences
        if (Preferences.loadPrefsString("GESTURE_DETECTION", "ON", this).equals("ON")) {
            tbSwipe.setChecked(true);

        }

        if (Preferences.loadPrefsString("WINDOWS_TRANSITIONS", "OFF", this).equals("ON")) {
            tbWindowTransitions.setChecked(true);

        }

        if (Preferences.loadPrefsString("ANIMATIONS", "OFF", this).equals("ON")) {
            tbAnimations.setChecked(true);

        }

        if (Preferences.loadPrefsString("CALL_MODE", "PHONE", this).equals("PHONE")) {
            tbAutoCall.setChecked(true);
        }

        if (Preferences.loadPrefsString("DETECT_CARRIER", "YES", this).equals("YES")) {
            tbCarrierCall.setChecked(true);
        }

        if (Preferences.loadPrefsString("ONLY_GRILL", "ON", getApplicationContext()).equals("ON")) {
            tbStores.setChecked(true);
        }

        if (Preferences.loadPrefsString("BASIC_CATEGORIES", "ON", getApplicationContext()).equals("ON")) {
            tbCategories.setChecked(true);
        }

        if (Preferences.loadPrefsString("MENU_STYLE", "NEW", getApplicationContext()).equals("CLASSIC")) {
            tbOldMenuView.setChecked(true);
        }

        //// Set listeners to toggle buttons and alert dialog text
        tbOldMenuView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Preferences.savePrefsString("MENU_STYLE", "CLASSIC", getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή θα προβάλει φωτογραφίες των καταλόγων",
                            "Προσοχή");
                } else {

                    Preferences.savePrefsString("MENU_STYLE", "NEW", getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή  θα χρησιμοποιεί κατηγορίες στην προβολή των καταλόγων.",
                            "Προσοχή");
                }
            }
        });

        tbSwipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Preferences.savePrefsString("GESTURE_DETECTION", "ON", getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή θα χρησιμοποιεί αναγνώριση gesture στο μενού των εστιατορίων.",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("GESTURE_DETECTION", "OFF", getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή δεν θα χρησιμοποιεί αναγνώριση gesture στο μενού των εστιατορίων.",
                            "Προσοχή");
                }
            }
        });

        tbWindowTransitions
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            Preferences.savePrefsString("WINDOWS_TRANSITIONS", "ON",
                                    getApplicationContext());
                            ShowAlertDialogOnScreen(
                                    "Η εφαρμογή θα χρησιμοποιεί τις δικές της μεταβάσεις οθονών και όχι του λειτουργικού. Για αδύναμες συσκευές συνιστάται να απενεργοποιήσετε αυτήν την επιλογή.",
                                    "Προσοχή");
                        } else {
                            Preferences.savePrefsString("WINDOWS_TRANSITIONS", "OFF",
                                    getApplicationContext());
                            ShowAlertDialogOnScreen(
                                    "Η εφαρμογή θα χρησιμοποιεί τις μεταβάσεις οθονών του λειτουργικού.",
                                    "Προσοχή");
                        }

                    }
                });

        tbAnimations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Preferences.savePrefsString("ANIMATIONS", "ON",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή θα χρησιμοποιεί γραφικά εφέ κατά την επιλογή κουμπιών. Για αδύναμες συσκευές συνιστάται να απενεργοποιήσετε αυτήν την επιλογή.",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("ANIMATIONS", "OFF",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή δεν θα χρησιμοποιεί γραφικά εφέ κατά την επιλογή κουμπιών.",
                            "Προσοχή");
                }

            }
        });

        tbStores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Preferences.savePrefsString("ONLY_GRILL", "ON",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "To Random θα αγνοεί τα καταστήματα που δεν κάνουν delivery ψητά.",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("ONLY_GRILL", "OFF",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Το Random λαμβάνει υπόψιν όλα τα καταστήματα.",
                            "Προσοχή");
                }

            }
        });
        tbCategories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Preferences.savePrefsString("BASIC_CATEGORIES", "ON",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "To Random θα αγνοεί κατηγορίες. π.χ: ποτά,ορεκτικά,σαλάτες",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("BASIC_CATEGORIES", "OFF",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Το Random λαμβάνει υπόψιν όλες τις κατηγορίες.",
                            "Προσοχή");
                }

            }
        });

        tbCarrierCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Preferences.savePrefsString("DETECT_CARRIER", "YES",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "H εφαρμογή θα επιλέγει τον αριθμό του εστιατορίου σύμφωνα με τον πάροχο τηλεφωνίας σας.",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("DETECT_CARRIER", "NO",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Η εφαρμογή θα εμφανίζει μια οθόνη με τα διαθέσιμα τηλέφωνα ώστε να επίλεξετε αυτό που επιθυμείτε να καλέσετε.",
                            "Προσοχή");
                }

            }
        });

        tbAutoCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Preferences.savePrefsString("CALL_MODE", "PHONE",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Όταν πιέζετε το εικονίδιο κλήση η εφαρμογή θα καλεί απευθείας τον αριθμό που επιλέξατε.",
                            "Προσοχή");
                } else {
                    Preferences.savePrefsString("CALL_MODE", "USER",
                            getApplicationContext());
                    ShowAlertDialogOnScreen(
                            "Όταν πιέζετε το εικονίδιο κλήση η εφαρμογή θα ανοίγει την εφαρμογή κλήσεων του κινητού σας  και θα εισάγει το τηλέφωνο που επιλέξατε περιμένοντας από εσάς να πατήσετε την κουμπί για την πραγματοποίηση της κλήσης.",
                            "Προσοχή");
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

    //// Shows an alert dialog on screen
    private void ShowAlertDialogOnScreen(String dialog, String title) {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setCustomImage(R.drawable.settings_gear)
                .setContentText(dialog)
                .setConfirmText("OK")
                .show();
    }
}
