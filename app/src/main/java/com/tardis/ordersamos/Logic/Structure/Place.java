package com.tardis.ordersamos.Logic.Structure;

import java.util.ArrayList;

/**
 * Created by Jim on 25/1/2015.
 */
public class Place {

    private ArrayList<Restaurant> restaurants = null;

    public Place(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }


    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Restaurant getSpecificRestaurantByName(String restaurantName){
        Restaurant toBeReturned = null;
        for(int i =0 ;i<restaurants.size();i++){
            if(restaurants.get(i).getName().equals(restaurantName)){
                toBeReturned = restaurants.get(i);
                break;
            }
        }

        return toBeReturned;
    }

    public ArrayList<Restaurant> getUnbannedRestaurants(){
        ArrayList<Restaurant> restaurantsToBeReturned = new ArrayList<Restaurant>();
        for(int i =0 ;i<restaurants.size();i++){
            if(restaurants.get(i).isBanned()==false){
                restaurantsToBeReturned.add(restaurants.get(i));
            }
        }

        return restaurantsToBeReturned;
    }


}
