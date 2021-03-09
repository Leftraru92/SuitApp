package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.suitapp.model.Province;
import com.example.suitapp.model.ShippingPrice;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.CaptureImageViewModel;
import com.example.suitapp.viewmodel.DialogSelectItemViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddStoreViewModel extends ViewModel implements DialogSelectItemViewModel, CaptureImageViewModel {

    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDesc;
    private MutableLiveData<String> mProvince;
    private MutableLiveData<Integer> mProvinceId;
    private MutableLiveData<String> mCity;
    private MutableLiveData<String> mStreet;
    private MutableLiveData<String> mNumber;
    private MutableLiveData<String> mFloor;
    private MutableLiveData<String> mApartment;
    private MutableLiveData<String> mAddress;
    private MutableLiveData<Bitmap> mImageLogo;
    private MutableLiveData<Bitmap> mImagePortada;
    private MutableLiveData<Boolean> mMailShipping;
    private MutableLiveData<Boolean> mPersonalShipping;
    private MutableLiveData<ArrayList<ShippingPrice>> mShippingPrice;
    ArrayList<ShippingPrice> mListShippingPrice;
    private MutableLiveData<ShippingPrice> mEditShippingPrice;

    public AddStoreViewModel() {
        mName = new MutableLiveData<>();
        mDesc = new MutableLiveData<>();
        mProvince = new MutableLiveData<>();
        mProvinceId = new MutableLiveData<>();
        mCity = new MutableLiveData<>();
        mStreet = new MutableLiveData<>();
        mNumber = new MutableLiveData<>();
        mFloor = new MutableLiveData<>();
        mApartment = new MutableLiveData<>();
        mAddress = new MutableLiveData<>();
        mImageLogo = new MutableLiveData<>();
        mImagePortada = new MutableLiveData<>();
        mMailShipping = new MutableLiveData<>();
        mPersonalShipping = new MutableLiveData<>();
        mShippingPrice = new MutableLiveData<>();
        mEditShippingPrice = new MutableLiveData<>();

        mListShippingPrice = new ArrayList<>();

        mProvinceId.setValue(0);
        mMailShipping.setValue(false);
        mPersonalShipping.setValue(false);
        mShippingPrice.setValue(mListShippingPrice);
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName.setValue(name);
    }

    public LiveData<String> getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        this.mDesc.setValue(desc);
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

    public LiveData<String> getAddress() {
        return mAddress;
    }

    public LiveData<Bitmap> getImageLogo() {
        return mImageLogo;
    }

    public void setImageLogo(Bitmap image) {
        this.mImageLogo.setValue(image);
    }

    public LiveData<Bitmap> getImagePortada() {
        return mImagePortada;
    }

    public void setmImagePortada(Bitmap image) {
        this.mImagePortada.setValue(image);
    }

    public LiveData<Boolean> getMailShipping() {
        return mMailShipping;
    }

    public void setMailShipping(boolean mailShipping) {
        this.mMailShipping.setValue(mailShipping);
    }

    public LiveData<Boolean> getPersonalShipping() {
        return mPersonalShipping;
    }

    public void setPersonalShipping(boolean personalShipping) {
        this.mPersonalShipping.setValue(personalShipping);
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
            case Constants.SELECT_SHIPPING:
                mEditShippingPrice.setValue(new ShippingPrice(new Province(id, value), -1));
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

    @Override
    public void setImage(Bitmap bitmap, int requestId) {
        switch (requestId) {
            case Constants.IMAGE_LOGO:
                setImageLogo(bitmap);
                break;
            case Constants.IMAGE_PORTADA:
                setmImagePortada(bitmap);
                break;
        }
    }

    @Override
    public void updateImage(Bitmap bitmap, int imageid) {
        setImage(bitmap, imageid);
    }

    public void addShippingPrice(int price) {
        //Si es editado primero lo elimino de la lista
        ArrayList<ShippingPrice> editedListSP = mListShippingPrice;
        for (ShippingPrice sp : mListShippingPrice) {
            if (sp.getProvince().getId() == mEditShippingPrice.getValue().getProvince().getId())
                editedListSP.add(sp);
        }
        mListShippingPrice.removeAll(editedListSP);
        //Lo guardo en la lista
        mEditShippingPrice.getValue().setPrice(price);
        mListShippingPrice.add(mEditShippingPrice.getValue());
        mShippingPrice.setValue(mListShippingPrice);

        mEditShippingPrice.setValue(null);
    }

    public LiveData<ArrayList<ShippingPrice>> getShippingPrice() {
        return mShippingPrice;
    }

    public void deleteShippingPrice(int position) {
        mListShippingPrice.remove(position);
        mShippingPrice.setValue(mListShippingPrice);
    }

    public void setEditShippingPrice(int position) {
        if (position == -1)
            mEditShippingPrice.setValue(null);
        else
            mEditShippingPrice.setValue(mListShippingPrice.get(position));
    }

    public LiveData<ShippingPrice> getEditShippingPrice() {
        return mEditShippingPrice;
    }

    @Override
    public LiveData<Bitmap> getImage(int image, int imageId) {
        switch (image) {
            case Constants.IMAGE_LOGO:
                return getImageLogo();
            case Constants.IMAGE_PORTADA:
                return getImagePortada();
            default:
                return null;
        }
    }

    public JSONObject toJSON(String hash){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", hash);
            jsonObject.put("storeName", getName().getValue());
            jsonObject.put("description", getDesc().getValue());

            JSONObject jsonAddress= new JSONObject();
            jsonAddress.put("province_id", getProvinceId().getValue());
            jsonAddress.put("city", getCity().getValue());
            jsonAddress.put("street", getStreet().getValue());
            jsonAddress.put("street_number", getNumber().getValue());
            jsonAddress.put("floor", getFloor().getValue());
            jsonAddress.put("apartment", getApartment().getValue());
            jsonObject.put("addressAdd", jsonAddress);

            jsonObject.put("storeLogo", (getImageLogo().getValue()==null) ? "" : Util.bitmapToBase64(getImageLogo().getValue()));
            jsonObject.put("storeCoverPhoto", (getImagePortada().getValue()== null) ? "" : Util.bitmapToBase64(getImagePortada().getValue()));
            jsonObject.put("physical_store", getPersonalShipping().getValue());
            jsonObject.put("mailShipping", getMailShipping().getValue());

            JSONArray jsonShipping= new JSONArray();
            for (ShippingPrice sp: getShippingPrice().getValue()) {
                JSONObject jsonPrice= new JSONObject();
                jsonPrice.put("idProvince", sp.getProvince().getId());
                jsonPrice.put("price", sp.getPrice());
                jsonShipping.put(jsonPrice);
            }
            jsonObject.put("shippingPrice", jsonShipping);

            return  jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}