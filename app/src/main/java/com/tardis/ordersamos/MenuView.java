package com.tardis.ordersamos;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.RotationAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Logic.Structure.Food;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;
import com.tardis.ordersamos.Utilities.SimpleGestureFilter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuView extends ActionBarActivity implements View.OnClickListener, SimpleGestureFilter.SimpleGestureListener {

    private Restaurant selectedRestaurant = null;
    private String previousActivity = "Stores";
    private View previousSelectedCategory;
    private LinearLayout llCategories, llFood;
    private int verticalThickness;
    private ImageView ivPhoneCall;
    private String phoneNumber;
    private SimpleGestureFilter detector;
    private HorizontalScrollView svCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

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

        //// Find and initialize ScrollView Categories
        svCategories = (HorizontalScrollView) findViewById(R.id.svCategories);

        //// Find and initialize imageView phone call
        ivPhoneCall = (ImageView) findViewById(R.id.ivCallFromCatalogue);

        //// Set onClickListener to imageView phone call
        ivPhoneCall.setOnClickListener(this);

        //// Find and initialize LinearLayout for the Food ScrollView
        llFood = (LinearLayout) findViewById(R.id.llFood);

        //// Find and initialize LinearLayout for the Categories ScrollView
        llCategories = (LinearLayout) findViewById(R.id.llCategories);

        //// Add Categories to the llCategories
        addCategories(llCategories, selectedRestaurant.getMenu().getCategories());

        //// Calculate cardView height
        verticalThickness = Methods.calculateComponentSize(222, 595, getResources());

        //// Changing containing food
        try {
            llFood.removeAllViews();
        } catch (Exception e) {
            Log.d("FIRST_TIME", "CHANGING CONTAINING FOOD");
        }

        ////  Initializing Gesture Filter
        detector = new SimpleGestureFilter(this, this);

        //// Set First Time Food
        String selectedCategory = selectedRestaurant.getMenu().getCategories().get(0);

        //// Add them to the layout
        addFoodCardView(llFood, selectedRestaurant.getMenu().getFoodByCategory(selectedCategory));
    }

    //// Adds all categories to layout
    public void addCategories(LinearLayout ll, ArrayList<String> re) {

        for (int i = 0; i < re.size(); i++) {
            Button bCategory = new Button(this);
            String categoryName = re.get(i);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT)
                bCategory.setAllCaps(false);
            bCategory.setText(categoryName);
            bCategory.setTypeface(null, Typeface.BOLD);
            bCategory.setTextColor(Color.parseColor("#ecf0f1"));
            bCategory.setId(i);
            bCategory.setBackgroundColor(Color.parseColor("#4f5d73"));
            bCategory.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    /* 255 */, LinearLayout.LayoutParams.WRAP_CONTENT));
            bCategory.setOnClickListener(this);
            ll.addView(bCategory);
            if (i == 0) {
                if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                    Preferences.savePrefsString("ANIMATIONS", "OFF",
                            getApplicationContext());
                    bCategory.performClick();
                    bCategory.setPressed(true);
                    bCategory.invalidate();
                    Preferences.savePrefsString("ANIMATIONS", "ON",
                            getApplicationContext());
                } else {
                    bCategory.performClick();
                    bCategory.setPressed(true);
                    bCategory.invalidate();
                }
            }
        }
    }

    //// Adds all foods that a category contains to a layout as cardViews with a specific appearance
    public void addFoodCardView(LinearLayout BanLayout,
                                ArrayList<Food> food) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < food.size(); i++) {
            CardView cvFood = new CardView(this);
            cvFood.setCardElevation((float) 15.0);
            cvFood.setRadius((float) 15.0);
            TextView tvFood = new TextView(this);
            String price = food.get(i).getPrice() + " €" + "\r\t";
            SpannableString redSpannable = new SpannableString(price);
            redSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#c75c5c")), 0,
                    price.length(), 0);
            builder.append(redSpannable);
            String foodName = food.get(i).getName() + "\r\n";
            SpannableString foodNameSpannable = new SpannableString(foodName);
            foodNameSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#4f5d73")),
                    0, foodName.length(), 0);
            String description = food.get(i).getDescription();
            /// System.out.println(description);
            SpannableString descriptionSpannable = new SpannableString(
                    description);
            descriptionSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#77b3d4")), 0,
                    description.length(), 0);
            if (food.get(i).getDescription().equals("-")) {
                builder.append(foodNameSpannable);
            } else {
                builder.append(foodNameSpannable);
                builder.append(descriptionSpannable);
            }
            tvFood.setText(builder, TextView.BufferType.SPANNABLE);
            tvFood.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            tvFood.setTypeface(null, Typeface.BOLD);
            tvFood.setId(i + 100);
            tvFood.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvFood.setHeight(verticalThickness);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                tvFood.setTextColor(Color.parseColor("#4f5d73"));
                tvFood.setAllCaps(false);
            }
            cvFood.addView(tvFood);
            BanLayout.addView(cvFood);
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Methods.getScreenDensity(getResources()) < 410) {
                cardLayoutParams.setMargins(12, 10, 12, 10);
            } else {
                cardLayoutParams.setMargins(12, 18, 12, 18);
            }
            cvFood.setLayoutParams(cardLayoutParams);
            builder.clear();

        }


    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.ivCallFromCatalogue) {
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

        } else {
            //// Disable button
            v.setClickable(false);

            //// Changing color to previous selected Category
            try {
                previousSelectedCategory.setBackgroundColor(Color.parseColor("#4f5d73"));
            } catch (Exception e) {
                Log.d("ERROR_FIRST_TIME", "CHANGING BUTTON BACKGROUND");
            }

            //// Setting new previous category THIS
            previousSelectedCategory = v;

            //// If animations are enabled
            if (Preferences.loadPrefsString("ANIMATIONS", "OFF", getApplicationContext()).equals("ON")) {
                new RotationAnimation(v)
                        .setDuration(200)
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //// Enable button again
                                v.setClickable(true);
                            }
                        })
                        .animate();
            } else {
                v.setClickable(true);
            }
            v.setBackgroundColor(Color.parseColor("#c75c5c"));

            //// Changing containing food
            try {
                llFood.removeAllViews();
            } catch (Exception e) {
                Log.d("ERROR_FIRST_TIME", "CHANGING CONTAINING FOOD");
            }

            String selectedCategory = selectedRestaurant.getMenu().getCategories().get(v.getId());

            addFoodCardView(llFood, selectedRestaurant.getMenu().getFoodByCategory(selectedCategory));
        }
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
        ivPhoneCall.setVisibility(View.VISIBLE);
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
        ivPhoneCall.setVisibility(View.VISIBLE);
    }

    //// Attaches the Listener to the detector
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {

        //// Call onTouchEvent of SimpleGestureFilter class
        if (Preferences.loadPrefsString("GESTURE_DETECTION", "ON", this).equals("ON")) {
            this.detector.onTouchEvent(me);
        }
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                if (previousSelectedCategory.getId() - 1 >= 0) {
                    Button button = (Button) findViewById(previousSelectedCategory.getId() - 1);
                    button.performClick();
                    button.requestFocus();
                    svCategories.smoothScrollTo(button.getLeft(), 0);
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                if (previousSelectedCategory.getId() + 1 < selectedRestaurant.getMenu().getCategories().size()) {
                    Button button = (Button) findViewById(previousSelectedCategory.getId() + 1);
                    button.performClick();
                    button.requestFocus();
                    svCategories.smoothScrollTo(button.getLeft(), 0);
                }
                str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                str = "Swipe Up";
                break;
        }
    }

    @Override
    public void onDoubleTap() {

    }
}
