package com.tardis.ordersamos.Utilities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;


import com.tardis.ordersamos.Logic.Structure.Food;
import com.tardis.ordersamos.Logic.Structure.Menu;
import com.tardis.ordersamos.Logic.Structure.Place;
import com.tardis.ordersamos.Logic.Structure.RandomFood;
import com.tardis.ordersamos.Logic.Structure.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jim on 25/1/2015.
 */
public class Methods extends Application {

    //// Method that calculates the height of a component based on screen DPI
    public static int calculateComponentSize(int firstNumber, int secondNumber, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        ////320 moto g 10-10
        return (firstNumber * metrics.densityDpi) / secondNumber;
    }

    //// Get Screen Density
    public static int getScreenDensity(Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.densityDpi;
    }

    //// Take Restaurant Name in String List
    public static List<String> getRestaurantNames(Context context) {

        //// Restaurants From DataBase
        ArrayList<Restaurant> restaurantsFromDataBase = getRestaurantsFromDatabase(context);

        //// List with Restaurant names
        List<String> restaurantNames = new ArrayList<String>();

        //// Adding Restaurant Name to List
        for (int i = 0; i < restaurantsFromDataBase.size(); i++) {
            restaurantNames.add(restaurantsFromDataBase.get(i).getName());

        }

        return restaurantNames;
    }

    //// Get Unbanned Restaurant names in an ArrayList
    public static List<String> getUnbannedRestaurantNames(
            ArrayList<Restaurant> list) {
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isBanned()==false)
                names.add(list.get(i).getName());
        }

      /*  for (int i = 0; i < list.size(); i++) {
            Log.d(" " + i + "-"
                    + list.get(i).getName(), "Banned: "
                    + list.get(i).isBanned());
        }*/

        for(int i = 0 ;i <names.size();i++){
            System.out.println(">>>>UNBANNED RESTAURANT"+names.get(i));
        }

        return names;
    }

    //// Ban a restaurant
    public static ArrayList<Restaurant> unBanRestaurant(String restaurantName, Context context,ArrayList<Restaurant> restaurants) {

        //// Get the restaurant list from the calling Method
        ArrayList<Restaurant>  restaurantsToBeDisplayed = restaurants;

        //// Getting the position for the restaurant we want to ban
        int position = -1;
        for (int i = 0; i < restaurantsToBeDisplayed.size(); i++) {
            if (restaurantsToBeDisplayed.get(i).getName().equals(restaurantName)) {
                position = i;
                break;
            }
        }

        //// Set Ban state to banned
        if (!(position == -1))
            restaurantsToBeDisplayed.get(position).setBanned(false);

        Preferences.writeList(context,
                Methods.getUnbannedRestaurantNames(restaurantsToBeDisplayed),
                "restnames");
        return restaurantsToBeDisplayed;
    }

    //// Ban a restaurant
    public static ArrayList<Restaurant> banRestaurant(String restaurantName, Context context,ArrayList<Restaurant> restaurants) {

        //// Get the restaurant list from the calling Method
        ArrayList<Restaurant>  restaurantsToBeDisplayed = restaurants;

        //// Getting the position for the restaurant we want to ban
        int position = -1;
        for (int i = 0; i < restaurantsToBeDisplayed.size(); i++) {
            if (restaurantsToBeDisplayed.get(i).getName().equals(restaurantName)) {
                position = i;
                break;
            }
        }

        //// Set Ban state to banned
        if (!(position == -1))
            restaurantsToBeDisplayed.get(position).setBanned(true);

        Preferences.writeList(context,
                Methods.getUnbannedRestaurantNames(restaurantsToBeDisplayed),
                "restnames");
        return restaurantsToBeDisplayed;
    }

    //// Method to Create A place so we can refer to Restaurants
    public static Place getThePlace(Context context) {

        Place Karlovasi = new Place(getRestaurantsAndTheirBanStateAndTheirMenu(context));

        return Karlovasi;
    }

    //// Returning Menu For A Restaurant
    public static Menu getResturantMenu(Context context, String tableName) {

        //// Creating an Object DataBaseHelper to access database
        DataBaseHelper myDbHelperer;
        myDbHelperer = new DataBaseHelper(context, "OrderAtSamosDB");

        //// Restaurant Menu to be Returned
        Menu menuToBeReturned;

        //// Creating an OrderAtSamos Database Instance
        try {
            myDbHelperer.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        //// Opening Database the Instance that creates and accessing Menu Data
        try {
            myDbHelperer.openDataBase();
            menuToBeReturned = myDbHelperer.getMenuFromDatabase(tableName);

        } catch (SQLException sqle) {

            throw sqle;
        }

        //// Closing the Database to prevent memory Leakage
        try {
            myDbHelperer.close();
        } catch (Exception e) {
            Log.e("DATABASE STATE", "NOT_CLOSED");
        }


        return menuToBeReturned;

    }

    //// Preparing Restaurants for Place Object
    public static ArrayList<Restaurant> getRestaurantsAndTheirBanStateAndTheirMenu(Context context) {

        //// ArrayList For the Restaurants coming from Database
        ArrayList<Restaurant> restaurantsFromDatabase;
        restaurantsFromDatabase = getRestaurantsFromDatabase(context);

        //// List For the Restaurants coming from Database
        List<String> restaurantsNamesFromPreferences;

        //// Getting Ban State for Every Restaurant
        restaurantsNamesFromPreferences = Preferences.readList(context, "restnames");

        //// Setting Ban State to Restaurants
        for (int i = 0; i < restaurantsFromDatabase.size(); i++) {
            for (int j = 0; j < restaurantsNamesFromPreferences.size(); j++) {
                if (restaurantsFromDatabase.get(i).getName().equals(restaurantsNamesFromPreferences.get(j))) {
                    restaurantsFromDatabase.get(i).setBanned(false);
                    break;

                } else {
                    restaurantsFromDatabase.get(i).setBanned(true);
                }
            }


        }
        //// Setting Menu for each Restaurant
        for (int i = 0; i < restaurantsFromDatabase.size(); i++) {
            restaurantsFromDatabase.get(i).setMenu(getResturantMenu(context, restaurantsFromDatabase.get(i).getTrueName()));
            System.out.println(">"+restaurantsFromDatabase.get(i).getName()+" is banned = "+restaurantsFromDatabase.get(i).isBanned());
        }

        return restaurantsFromDatabase;
    }


    //// Used for getting The Restaurants for The Splash Saving Ban State for FIRST_TIME and the
    //// 'getRestaurantsAndTheirBanState(Context context)' method in Methods
    @SuppressLint("LongLogTag")
    public static ArrayList<Restaurant> getRestaurantsFromDatabase(Context context) {
        //// Creating an Object DataBaseHelper to access database
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(context, "OrderAtSamosDB");

        //// ArrayList For the Restaurants coming from Database
        ArrayList<Restaurant> restaurantsFromDatabase;


        //// Creating an OrderAtSamos Database Instance
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        //// Opening Database the Instance that creates and accessing Restaurant Data
        try {
            myDbHelper.openDataBase();
            restaurantsFromDatabase = myDbHelper.getRestaurantsFromDatabase();


        } catch (SQLException sqle) {

            throw sqle;
        }

        //// Closing the Database to prevent memory Leakage
        try {
            myDbHelper.close();
        } catch (Exception e) {
            Log.e("DATABASE STATE REFERENCE", "NOT_CLOSED");
        }

        return restaurantsFromDatabase;
    }

    //// Return the restauran that was selected by the user
    public static Restaurant getSelectedRestaurant(Context context) {


        //// Getting restaurant list from Database as place karlovasi
        Place Karlovasi = new Place(Methods.getRestaurantsAndTheirBanStateAndTheirMenu(context));

        //// Getting selected restaurant position
        int selectedRestaurantPosition = Preferences.loadPrefsInt("SELECTED_RESTAURANT", 0, context);

        //// Return selected Restaurant KOUZINA FIRST Remove Methods Call to get back to normal

        return Methods.changeRestaurantPosition(Karlovasi.getUnbannedRestaurants(),"Kouzina", 0).get(selectedRestaurantPosition);
    }


    // Method to open dialer and dial a phone
    public static void phoneCall(String phone, Context context) {
        String phoneCallUri = "tel:" + phone;
        Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
        phoneCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneCallIntent.setData(Uri.parse(phoneCallUri));
        context.startActivity(phoneCallIntent);
    }

    // Method to Call a phone directly
    public static void phoneCallAutoCall(String phone, Context context) {
        String phoneCallUri = "tel:" + phone;
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneCallIntent.setData(Uri.parse(phoneCallUri));
        context.startActivity(phoneCallIntent);
    }

    //// Returns the carriers phone number or NULL
    public static String getCarrierPhoneNumber(Context context, Context baseContext) {

        String carrierName = Methods.getCarrierName(baseContext);
        String phone = null;
        Restaurant rest = Methods.getSelectedRestaurant(context);

        if (carrierName.equals("COSMOTE")) {
            phone = rest.getPhoneCosmote();
        } else if (carrierName.equals("VODAFONE")) {
            phone = rest.getPhoneVodafone();
        } else if (carrierName.equals("WIND")) {
            phone = rest.getPhoneWind();
        } else if (carrierName.equals("NO_CARRIER")) {
            phone = null;
        }

        try {
            if (phone != null)
                phone.replace(" ", "");
        } catch (Exception e) {
            Log.d("SPACE DETECTING", "FAILED");
        }

        if (phone.equals("-")) {
            phone = null;
        }
        System.out.println(">" + phone + "<");
        return phone;
    }

    // Get carrier name (Network Operator Name)
    @SuppressLint("DefaultLocale")
    public static String getCarrierName(Context context) {
        String toBeReturned;
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = tManager.getNetworkOperatorName();
        carrierName = carrierName.toUpperCase();
        if (carrierName.contains("COSMOTE")) {
            toBeReturned = "COSMOTE";
        } else if (carrierName.contains("VODAFONE")) {
            toBeReturned = "VODAFONE";
        } else if (carrierName.contains("WIND") || carrierName.contains("TIM")) {
            toBeReturned = "WIND";
        } else {
            toBeReturned = "NO_CARRIER";
        }
        System.out.println(">" + toBeReturned + "<");
        return toBeReturned;
    }

    // Get Phone model and manufacturer name
    public static String getPhoneManufacturerAndModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }


    // allazei tin thesi emfanisis enos estiatoriou
    public static ArrayList<Restaurant> changeRestaurantPosition(
            ArrayList<Restaurant> restaurant, String restaurantTrueName,
            int newPosition) {
        for (int i = 0; i < restaurant.size(); i++) {
            if (restaurant.get(i).getTrueName().equals(restaurantTrueName)) {
                Restaurant temp = restaurant.get(i);
                restaurant.remove(i);

                restaurant.add(newPosition, temp);
                break;
            }
        }
        return restaurant;
    }

    // epistrefei ena random fagito
    public static RandomFood getRandomFood(ArrayList<Restaurant> res,
                                           Context context) {
        int x;
        Restaurant rest;
        Menu menu = null;
        Food food;
        ArrayList<String> categories = new ArrayList<String>();
        String selectedCategory;
        ArrayList<Food> categoryFood = new ArrayList<Food>();
        if (Preferences.loadPrefsString("ONLY_GRILL", "ON",context).equals("ON")) {

            for (int i = 0; i < res.size(); i++) {

                if (res.get(i).getTrueName().equals("Periptero")) {
                    res.remove(i);
                }
            }
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).getTrueName().equals("Tzamas")) {
                    res.remove(i);
                }
            }
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).getTrueName().equals("Plaisir")) {
                    res.remove(i);
                }
            }
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).getTrueName().equals("Deli")) {
                    res.remove(i);
                }

            }
        }

        try {
            x = randInt(0, res.size() - 1);
            rest = res.get(x);
            categories = res.get(x).getMenu().getCategories();
            menu = res.get(x).getMenu();
        } catch (Exception e) {
            Log.d("CRASH", "Getting Menu");
            return getRandomFood(res, context);
        }


        if (Preferences.loadPrefsString("BASIC_CATEGORIES", "ON", context).equals("ON")) {
            if (categories.contains(("Ποτά"))) {
                categories.remove(categories.indexOf("Ποτά"));
            }
            if (categories.contains(("Ορεκτικά"))) {
                categories.remove(categories.indexOf("Ορεκτικά"));
            }
            if (categories.contains(("Σαλάτες"))) {
                categories.remove(categories.indexOf("Σαλάτες"));
            }
            if (categories.contains(("Υλικά"))) {
                categories.remove(categories.indexOf("Υλικά"));
            }
            if (categories.contains(("Ομελέτες"))) {
                categories.remove(categories.indexOf("Ομελέτες"));
            }
            if (categories.contains(("Προσφορές"))) {
                categories.remove(categories.indexOf("Προσφορές"));
            }
            if (categories.contains(("Ημέρας"))) {
                categories.remove(categories.indexOf("Ημέρας"));
            }
            if (categories.contains(("Ζεστά ορεκτικά"))) {
                categories.remove(categories.indexOf("Ζεστά ορεκτικά"));
            }
            if (categories.contains(("Γλυκά"))) {
                categories.remove(categories.indexOf("Γλυκά"));
            }
            if (categories.contains(("Σάντουιτς ζεστά"))) {
                categories.remove(categories.indexOf("Σάντουιτς ζεστά"));
            }
            if (categories.contains(("Τυριά"))) {
                categories.remove(categories.indexOf("Τυριά"));
            }
            if (categories.contains(("Αλοιφές"))) {
                categories.remove(categories.indexOf("Αλοιφές"));
            }
        }


        try {
            x = randInt(0, categories.size() - 1);
            selectedCategory = categories.get(x);
            categoryFood = menu.getFoodByCategory(selectedCategory);
        } catch (Exception e) {
            Log.d("CRASH", "Getting Category");
            return getRandomFood(res, context);

        }


        for (int i = 0; i < categoryFood.size(); i++) {
            Log.d("Price", categoryFood.get(i).getPrice() + "");
            if (categoryFood.get(i).getPrice().equals("-")) {
                categoryFood.remove(i);
            }
        }
        for (int i = 0; i < categoryFood.size(); i++) {
            if (categoryFood.get(i).getPrice().equals("0,00")) {
                categoryFood.remove(i);
            }
        }
        for (int i = 0; i < categoryFood.size(); i++) {
            if (categoryFood.get(i).getPrice().equals("+")) {
                categoryFood.remove(i);
            }

        }


        try {
            x = randInt(0, categoryFood.size() - 1);
            food = categoryFood.get(x);
        } catch (Exception e) {
            Log.d("CRASH", "Getting Food");
            return getRandomFood(res, context);
        }

        Log.d("RES", "--> " + rest.getName());
        Log.d("FOOD", "--> " + food.getName());

        return new RandomFood(rest, food);


    }

    // Create Random Int In Range
    public static int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}

