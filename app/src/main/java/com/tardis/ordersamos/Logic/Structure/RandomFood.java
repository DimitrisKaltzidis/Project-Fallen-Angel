package com.tardis.ordersamos.Logic.Structure;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class RandomFood {

    private Restaurant res;
    private Food food;



    public RandomFood(Restaurant res, Food food) {
        this.res=res;
        this.food=food;

    }

    public String getRestaurantName(){
        return res.getName();
    }

    public Restaurant getRestaurant(){
        return res;
    }

    public String getFoodName(){
        return food.getName();
    }
    public String getFoodPrice(){
        return food.getPrice();
    }
    public String getFoodCategory(){
        return food.getCategory();
    }
    public String getFoodDescription(){
        return food.getDescription();
    }

    public String getFoodNameAndPrice(){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        try{
            builder.clear();
        }catch(Exception e){

        }
        String price = food.getPrice() + " €" ;
        SpannableString redSpannable = new SpannableString(price);
        redSpannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#c75c5c")), 0,
                price.length(), 0);
        builder.append(redSpannable);
        return food.getName()+" "+builder;
    }

}
