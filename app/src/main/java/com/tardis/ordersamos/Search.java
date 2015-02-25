package com.tardis.ordersamos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.tardis.ordersamos.Logic.Structure.Food;
import com.tardis.ordersamos.Logic.Structure.Place;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Utilities.Methods;
import com.tardis.ordersamos.Utilities.Preferences;

import java.util.ArrayList;
import java.util.List;


public class Search extends ActionBarActivity {

  /*  private Spinner spinRestaurant, spinCategories;
    private Button bSearchCall;
    private LinearLayout llSearch;
    private ArrayList<Restaurant> shownRestaurants;
    private TextView tvUnban;
    private ImageView ivUnban;
    private CardView cardViewBan;
    private List<String> restaurantStrings ;*/

    private String phoneNumber;
    Spinner spinRestaurant, spinCategories;
    Button bSearchCall;
    LinearLayout llSearch;
    Restaurant restaurantObject;
    String selectedRestaurant, selectedCategory;

    ArrayList<Restaurant> shownRestaurants = new ArrayList<Restaurant>();

    List<String> restaurantStrings = new ArrayList<String>();
    List<String> categoriesStrings = new ArrayList<String>();
    ArrayAdapter<String> adpterCategoriesStrings;
    Toast toast;
    CardView cardViewBan;
    ImageView ivUnban;
    TextView tvUnban;
    int verticalThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinRestaurant = (Spinner) findViewById(R.id.spinRestaurant);
        spinCategories = (Spinner) findViewById(R.id.spinCategory);
        bSearchCall = (Button) findViewById(R.id.ivCallFromSearch);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        tvUnban = (TextView) findViewById(R.id.tvUnbanSearch);
        ivUnban = (ImageView) findViewById(R.id.ivUnbanSearch);

        Place Karlovasi = Methods.getThePlace(getApplicationContext());
        shownRestaurants = Methods.changeRestaurantPosition(Karlovasi.getUnbannedRestaurants(), "Kouzina", 0);


        //// Calculate cardView height
        verticalThickness = Methods.calculateComponentSize(222, 595, getResources());

        tvUnban.setVisibility(View.GONE);
        ivUnban.setVisibility(View.GONE);
//        cardViewBan.setVisibility(View.GONE);

        llSearch.removeAllViews();
        for (int i = 0; i < shownRestaurants.size(); i++) {
            restaurantStrings.add(shownRestaurants.get(i).getName());
        }

        bSearchCall.setOnClickListener(new View.OnClickListener() {
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
                int value = Preferences.loadPrefsInt(restaurantObject.getName() + "_ORDERS", -1,
                        getApplicationContext());
                Preferences.savePrefsInt(restaurantObject.getName() + "_ORDERS", value + 1, getApplicationContext());
            }
        });


        ArrayAdapter<String> adpterRestaurantStrings = new ArrayAdapter<String>(
                this, R.layout.spinner_item, restaurantStrings);
        adpterCategoriesStrings = new ArrayAdapter<String>(this,
                R.layout.spinner_item, categoriesStrings);
        adpterRestaurantStrings
                .setDropDownViewResource(R.layout.spinner_dropdown_item);
        adpterCategoriesStrings
                .setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinCategories.setAdapter(adpterCategoriesStrings);
        spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinCategories.setSelection(position);
                selectedCategory = (String) spinCategories.getSelectedItem();
                /*
                 * Toast.makeText(getApplicationContext(), "petixe: " +
				 * selectedCategory, Toast.LENGTH_SHORT) .show();
				 */
                ArrayList<Food> foodToDisplay = new ArrayList<Food>();

                foodToDisplay = restaurantObject.getMenu().getFoodByCategory(
                        selectedCategory);

                try {
                    llSearch.removeAllViews();
                } catch (Exception e) {

                }
                addFoodCardView(llSearch, foodToDisplay);

                // spinCategories

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spinRestaurant.setAdapter(adpterRestaurantStrings);
        spinRestaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinRestaurant.setSelection(position);
                selectedRestaurant = (String) spinRestaurant.getSelectedItem();

                for (int i = 0; i < shownRestaurants.size(); i++) {
                    if (shownRestaurants.get(i).getName()
                            .equals(selectedRestaurant)) {
                        restaurantObject = shownRestaurants.get(i);
                        break;
                    }

                }


                categoriesStrings = restaurantObject.getMenu().getCategories();

                adpterCategoriesStrings = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.spinner_item,
                        categoriesStrings);
                spinCategories.setAdapter(adpterCategoriesStrings);
                adpterCategoriesStrings
                        .setDropDownViewResource(R.layout.spinner_dropdown_item);
                // spinCategories

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

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

    //// Method that controls a call to a restaurant
    public void callRestaurant(Context context, Context baseContext) {

        if (Preferences.loadPrefsString("DETECT_CARRIER", "YES", this).equals("YES")) {
            phoneNumber = Methods.getCarrierPhoneNumber(context, baseContext);
            if (phoneNumber == null) {
                Toast.makeText(context, "Το κατάστημα δεν διαθέτει τηλέφωνο " + Methods.getCarrierName(context), Toast.LENGTH_LONG).show();
                createCallDialog(restaurantObject.getPhoneNumbersInAnStringArray(), context);

            } else {
                if (Preferences.loadPrefsString("CALL_MODE", "PHONE", context).equals("PHONE")) {
                    Methods.phoneCallAutoCall(phoneNumber, getApplicationContext());
                } else {
                    Methods.phoneCall(phoneNumber, context);
                }
            }
        } else {
            createCallDialog(restaurantObject.getPhoneNumbersInAnStringArray(), context);
        }
        bSearchCall.setVisibility(View.VISIBLE);
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
        bSearchCall.setVisibility(View.VISIBLE);
    }

    public void addFoodCardView(LinearLayout BanLayout,
                                ArrayList<Food> food) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 0; i < food.size(); i++) {

            CardView cvFood = new CardView(this);
            TextView tvFood = new TextView(this);

            cvFood.setCardElevation((float) 15.0);
            cvFood.setRadius((float) 15.0);
            String price = food.get(i).getPrice() + " €" + "		 ";
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
            SpannableString descriptionSpannable = new SpannableString(
                    description);
            descriptionSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#77b3d4")), 0,
                    description.length(), 0);

            if (food.get(i).getDescription().equals("-")) {
                builder.append(foodNameSpannable);
            } else {
                //
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
    protected void onResume() {
        super.onResume();

    }
}
