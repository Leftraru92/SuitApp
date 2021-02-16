package com.example.suitapp.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ShippingPrice {
    private Province province;
    private int price;

    public ShippingPrice(Province province, int price) {
        this.province = province;
        this.price = price;
    }

    public Province getProvince() {
        return province;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###.##", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
