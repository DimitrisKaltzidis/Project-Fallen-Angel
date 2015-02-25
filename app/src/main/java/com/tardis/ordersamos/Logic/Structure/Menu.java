package com.tardis.ordersamos.Logic.Structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jim on 25/1/2015.
 */
public class Menu {
    private ArrayList<Food> menu = null;

    public Menu(ArrayList<Food> menu) {
        this.menu = menu;
    }

    //// Returns Categories on a ArrayList
    public ArrayList<String> getCategories() {

        ArrayList<String> toBeReturned = new ArrayList<String>();

        for (int i = 0; i < menu.size(); i++) {
            String str = menu.get(i).getCategory();

            // str = str.replaceAll("(\\r|\\n)", "");
            toBeReturned.add(str);
            //Log.d("ALL Category ", "--> " + toBeReturned.get(i));

        }
        Set<String> set = new HashSet<String>(toBeReturned);
        for (@SuppressWarnings("unused") String temp : set) {
            //	Log.d("SET Category ", "--> " + temp);
        }

        toBeReturned = new ArrayList<String>(set);

        for (int i = 0; i < toBeReturned.size(); i++) {
            //  Log.d("ARRAYLIST Category ", "--> " + toBeReturned.get(i));
        }


        toBeReturned = rearrangeCategories(toBeReturned);

        return toBeReturned;

    }

    // Rearrange Categories
    public ArrayList<String> rearrangeCategories(ArrayList<String> toBeReturned) {

        int thesi = 0;
        if (toBeReturned.contains("Προσφορές")) {
            String temp = toBeReturned.get(getCategoryNamePosition("Προσφορές",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Προσφορές",
                    toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }
        if (toBeReturned.contains("Πίτες")) {
            String temp = toBeReturned.get(getCategoryNamePosition("Πίτες",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Πίτες", toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }

        if (toBeReturned.contains("Σάντουϊτς")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Σάντουϊτς",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Σάντουϊτς",
                    toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }

        if (toBeReturned.contains("Σάντουιτς")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Σάντουιτς",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Σάντουιτς",
                    toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }
        if (toBeReturned.contains("Πίτσες")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Πίτσες",
                    toBeReturned));
            toBeReturned
                    .remove(getCategoryNamePosition("Πίτσες", toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }

        if (toBeReturned.contains("Μπέργκερ")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Μπέργκερ",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Μπέργκερ",
                    toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }

        if (toBeReturned.contains("Ζυμαρικά")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Ζυμαρικά",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Ζυμαρικά",
                    toBeReturned));
            toBeReturned.add(thesi, temp);
            thesi++;

        }

        if (toBeReturned.contains("Ποτά")) {

            String temp = toBeReturned.get(getCategoryNamePosition("Ποτά",
                    toBeReturned));
            toBeReturned.remove(getCategoryNamePosition("Ποτά",
                    toBeReturned));
            toBeReturned.add(toBeReturned.size(), temp);


        }

        return toBeReturned;

    }

    /// Returns the position of a category within a list
    public int getCategoryNamePosition(String categoryName,
                                       ArrayList<String> categories) {
        int categoryPosition = -1;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(categoryName)) {
                categoryPosition = i;
            }
        }
        return categoryPosition;

    }

    //// Returns all foods from a specific category
    public ArrayList<Food> getFoodByCategory(String categoryName) {
        ArrayList<Food> toBeReturned = new ArrayList<Food>();
        for (int i = 0; i < menu.size(); i++) {

            if (menu.get(i).getCategory().equals(categoryName)) {
                toBeReturned.add(menu.get(i));
            }
        }

        return toBeReturned;
    }

    //// Returns all foods with a specific price
    public ArrayList<Food> getFoodByPrice(String price) {
        ArrayList<Food> toBeReturned = new ArrayList<Food>();
        if (price.contains(".")) {
            price.replace(".", ",");
        }

        for (int i = 0; i < menu.size(); i++) {

            if (menu.get(i).getPrice().equals(price)) {
                toBeReturned.add(menu.get(i));
            }
        }

        return toBeReturned;
    }

}
