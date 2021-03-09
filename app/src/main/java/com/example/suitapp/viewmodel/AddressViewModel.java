package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;

import com.example.suitapp.model.Province;
import com.example.suitapp.model.ShippingPrice;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressViewModel extends ViewModel implements DialogSelectItemViewModel {

    private MutableLiveData<String> mProvince;
    private MutableLiveData<Integer> mProvinceId;
    private MutableLiveData<String> mCity;
    private MutableLiveData<Integer> mCp;
    private MutableLiveData<String> mStreet;
    private MutableLiveData<String> mNumber;
    private MutableLiveData<String> mFloor;
    private MutableLiveData<String> mApartment;
    private MutableLiveData<String> mAddress;
    private MutableLiveData<String> mDetail;

    public AddressViewModel() {
        mProvince = new MutableLiveData<>();
        mProvinceId = new MutableLiveData<>();
        mCity = new MutableLiveData<>();
        mCp = new MutableLiveData<>();
        mStreet = new MutableLiveData<>();
        mNumber = new MutableLiveData<>();
        mFloor = new MutableLiveData<>();
        mApartment = new MutableLiveData<>();
        mAddress = new MutableLiveData<>();
        mDetail = new MutableLiveData<>();

        mProvinceId.setValue(0);
    }

    public LiveData<String> getProvince() {
        return mProvince;
    }

    public LiveData<Integer> getProvinceId() {
        return mProvinceId;
    }

    public void setProvince(String province) {
        this.mProvince.setValue(province);
        setAddress();
    }

    public LiveData<String> getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity.setValue(city);
        setAddress();
    }

    public LiveData<String> getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        this.mStreet.setValue(street);
        setAddress();
    }

    public LiveData<String> getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber.setValue(number);
        setAddress();
    }

    public LiveData<String> getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        this.mFloor.setValue(floor);
        setAddress();
    }

    public LiveData<String> getApartment() {
        return mApartment;
    }

    public void setApartment(String apartment) {
        this.mApartment.setValue(apartment);
        setAddress();
    }

    public LiveData<Integer> getCp() {
        return mCp;
    }

    public void setCp(int cp) {
        this.mCp.setValue(cp);
    }

    public LiveData<String> getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        this.mDetail.setValue(detail);
    }

    public LiveData<String> getAddress() {
        return mAddress;
    }

    private void setAddress() {
        String address = "";
        address += (getStreet().getValue() == null || getStreet().getValue().isEmpty()) ? "" : getStreet().getValue() + " ";
        address += (getNumber().getValue() == null || getNumber().getValue().isEmpty()) ? "" : getNumber().getValue() + " ";
        address += (getFloor().getValue() == null || getFloor().getValue().isEmpty()) ? "" : " (" + getFloor().getValue() + "° " + ((getApartment().getValue() == null || getApartment().getValue().isEmpty()) ? ") " : getApartment().getValue() + ") ");
        address += (address != "") ? ", " : "";
        address += getCity().getValue() + ", " + getProvince().getValue();
        this.mAddress.setValue(address);
    }


    @Override
    public void selectValue(int id, String value, int requestId) {
        switch (requestId) {
            case Constants.SELECT_PROVINCE:
                this.mProvinceId.setValue(id);
                this.mProvince.setValue(value);
                break;
        }
    }

    @Override
    public int getSelectValue(int requestId) {
        switch (requestId) {
            case Constants.SELECT_PROVINCE:
                return mProvinceId.getValue();
            default:
                return 0;
        }
    }


    public JSONObject toJSON(String hash){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", hash);
            jsonObject.put("province_id", getProvinceId().getValue());
            jsonObject.put("city", getCity().getValue());
            jsonObject.put("street", getStreet().getValue());
            jsonObject.put("street_number", getNumber().getValue());
            jsonObject.put("floor", getFloor().getValue());
            jsonObject.put("apartment", getApartment().getValue());
            jsonObject.put("postal_code", getCp().getValue());
            jsonObject.put("description", getDetail().getValue());

            return  jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clean() {
        mProvince = new MutableLiveData<>();
        mProvinceId = new MutableLiveData<>();
        mCity = new MutableLiveData<>();
        mCp = new MutableLiveData<>();
        mStreet = new MutableLiveData<>();
        mNumber = new MutableLiveData<>();
        mFloor = new MutableLiveData<>();
        mApartment = new MutableLiveData<>();
        mAddress = new MutableLiveData<>();
        mDetail = new MutableLiveData<>();

        mProvinceId.setValue(0);

    }
}