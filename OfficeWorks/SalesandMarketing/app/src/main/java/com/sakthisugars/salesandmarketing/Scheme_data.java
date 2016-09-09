package com.sakthisugars.salesandmarketing;

/**
 * Created by singapore on 23-06-2016.
 */
public class Scheme_data {

    private String main_items,offer_items;
    private int discount_value, discount_value_type;
    public String getMain_items() {
        return main_items;
    }

    public int getDiscount_value() {
        return discount_value;
    }

    public int getDiscount_value_type() {
        return discount_value_type;
    }

    public String getOffer_items() {
        return offer_items;
    }

    public void setDiscount_value(int discount_value) {
        this.discount_value = discount_value;
    }

    public void setDiscount_value_type(int discount_value_type) {
        this.discount_value_type = discount_value_type;
    }

    public void setMain_items(String main_items) {
        this.main_items = main_items;
    }

    public void setOffer_items(String offer_items) {
        this.offer_items = offer_items;
    }
}
