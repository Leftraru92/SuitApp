package com.example.suitapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Address {
    int id;
    int provinceId;
    String city;
    String streetName;
    int streetNumber;
    int postalCode;
    String floor;
    String appartment;
    String description;
    String provinceName;

    public Address(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("address_id");
        provinceId = dataItem.getInt("province_id");
        city = dataItem.getString("city");
        streetName = dataItem.getString("street");
        streetNumber = dataItem.getInt("street_number");

        if (dataItem.has("postal_code") && dataItem.getString("postal_code") != null && !dataItem.getString("postal_code").equals("null"))
            postalCode = dataItem.getInt("postal_code");

        if (dataItem.has("floor") && dataItem.getString("floor") != null && !dataItem.getString("floor").equals("null"))
            floor = dataItem.getString("floor");

        if (dataItem.has("appartment") && dataItem.getString("appartment") != null && !dataItem.getString("appartment").equals("null"))
            appartment = dataItem.getString("appartment");

        if (dataItem.has("description") && dataItem.getString("description") != null && !dataItem.getString("description").equals("null"))
            description = dataItem.getString("description");

        for (Province prov : ProvinceList.getProvinces()) {
            if (prov.getId() == provinceId) {
                provinceName = prov.getName();
                break;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getDescription() {
        if (description.equals(""))
            return "Sin detalle";
        else
            return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAddress() {
        return getStreetName() + " " + getStreetNumber();
    }

    public String getAddressDetail() {
        return getDescription() + " - C.P. " + getPostalCode() + " - " +
                getCity() + ", " + getProvinceName();
    }
}
