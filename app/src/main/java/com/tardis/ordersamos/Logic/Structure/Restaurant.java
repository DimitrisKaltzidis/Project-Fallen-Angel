package com.tardis.ordersamos.Logic.Structure;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim on 25/1/2015.
 */
public class Restaurant {

    private String trueName, name, description, phone, phoneCosmote,
            phoneVodafone, phoneWind, closeDay, openingHour, closingHour,
            image, facebook, address;

    private boolean banned;

    private Menu menu;


    public Restaurant(String trueName, String name, String description,
                      String phone, String phoneCosmote, String phoneVodafone,
                      String phoneWind, String closeDay, String openingHour,
                      String closingHour, String image, String facebook, String address, boolean banned) {
        this.name = name;
        this.trueName = trueName;
        this.banned = banned;
        this.description = description;
        this.phone = phone;
        this.phoneCosmote = phoneCosmote;
        this.phoneVodafone = phoneVodafone;
        this.phoneWind = phoneWind;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.image = image;
        this.closeDay = closeDay;
        this.facebook = facebook;
        this.address = address;
    }


    public String getFacebook() {
        return facebook;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getTrueName() {
        return trueName;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneCosmote() {
        return phoneCosmote;
    }

    public String getPhoneVodafone() {
        return phoneVodafone;
    }

    public String getPhoneWind() {
        return phoneWind;
    }

    public String getCloseDay() {
        return closeDay;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public String getImage() {
        return image;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    // return all phone numbers as a String Array
    public String[] getPhoneNumbersInAnStringArray() {

      List<String> phones = new ArrayList<String>();

        phones.add("Σταθερό : " + getPhone());
        phones.add("Cosmote : " + getPhoneCosmote());
        phones.add("Vodafone : " + getPhoneVodafone());
        phones.add("Wind : " + getPhoneWind());

        for (int i = 0; i < phones.size(); i++) {
            if (phones.get(i).contains("-")) {
                phones.remove(i);
            }
        }

        String[] stringArray = phones.toArray(new String[phones.size()]);

        return stringArray;
    }
}
