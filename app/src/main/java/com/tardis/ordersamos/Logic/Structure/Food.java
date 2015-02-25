package com.tardis.ordersamos.Logic.Structure;

/**
 * Created by Jim on 25/1/2015.
 */

//// Represents a food on Restaurant Menu
public class Food {


    private String name, description, category, price;
    private String restaurantName;

    public Food(String name, String description, String category, String price) {

        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
