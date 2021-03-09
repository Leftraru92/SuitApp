package com.example.suitapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ShippingPrice implements Parcelable {
    private Province province;
    private int price;

    public ShippingPrice(Province province, int price) {
        this.province = province;
        this.price = price;
    }

    public ShippingPrice(JSONObject dataItem, int i) throws JSONException {
        int id = dataItem.getInt("idProvince");
        String name  = dataItem.getString("nameProvince");
        price = dataItem.getInt("price");
        Province province = new Province(id, name);
        this.province = province;
    }

    protected ShippingPrice(Parcel in) {
        province = in.readParcelable(Province.class.getClassLoader());
        price = in.readInt();
    }

    public static final Creator<ShippingPrice> CREATOR = new Creator<ShippingPrice>() {
        @Override
        public ShippingPrice createFromParcel(Parcel in) {
            return new ShippingPrice(in);
        }

        @Override
        public ShippingPrice[] newArray(int size) {
            return new ShippingPrice[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(province, flags);
        dest.writeInt(price);
    }
}
