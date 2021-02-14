package com.example.suitapp.fragment.addStore;

import android.graphics.Bitmap;

import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.CaptureImageViewModel;
import com.example.suitapp.viewmodel.DialogSelectViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddStoreViewModel extends ViewModel implements DialogSelectViewModel, CaptureImageViewModel {

    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDesc;
    private MutableLiveData<String> mProvince;
    private MutableLiveData<Integer> mProvinceId;
    private MutableLiveData<String> mCity;
    private MutableLiveData<String> mStreet;
    private MutableLiveData<String> mNumber;
    private MutableLiveData<String> mFloor;
    private MutableLiveData<String> mApartment;
    private MutableLiveData<Bitmap> mImageLogo;
    private MutableLiveData<Bitmap> mImagePortada;

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
        mImageLogo = new MutableLiveData<>();
        mImagePortada = new MutableLiveData<>();

        mProvinceId.setValue(0);
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

    public void setProvince(String province) {
        this.mProvince.setValue(province);
    }

    public LiveData<String> getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity.setValue(city);
    }

    public LiveData<String> getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        this.mStreet.setValue(street);
    }

    public LiveData<String> getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber.setValue(number);
    }

    public LiveData<String> getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        this.mFloor.setValue(floor);
    }

    public LiveData<String> getApartment() {
        return mApartment;
    }

    public void setApartment(String apartment) {
        this.mApartment.setValue(apartment);
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


    @Override
    public void selectValue(int id, String value) {
        this.mProvinceId.setValue(id);
        this.mProvince.setValue(value);
    }

    @Override
    public int getSelectValue() {
        return mProvinceId.getValue();
    }

    @Override
    public void setImage(Bitmap bitmap, int id) {
        switch (id) {
            case Constants.IMAGE_LOGO:
                setImageLogo(bitmap);
                break;
            case Constants.IMAGE_PORTADA:
                setmImagePortada(bitmap);
                break;
        }
    }
}