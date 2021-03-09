package com.example.suitapp.viewmodel;

import com.example.suitapp.model.Article;
import com.example.suitapp.model.Package;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingViewModel extends ViewModel {

    private MutableLiveData<List<Package>> mPackages;
    private List<Package> packageList;
    private MutableLiveData<Integer> mShippingPrice;
    private MutableLiveData<Integer> mProvinceId;

    public ShoppingViewModel() {
        mPackages = new MutableLiveData<>();
        packageList = new ArrayList<>();
        mPackages.setValue(packageList);
        mShippingPrice = new MutableLiveData<>();
        mProvinceId = new MutableLiveData<>();

        mProvinceId.setValue(0);
    }

    public LiveData<List<Package>> getPackages() {
        return mPackages;
    }

    public void setPackages(List<Package> packageList) {
        this.mPackages.setValue(packageList);
    }

    public boolean isAllShippingSelected() {
        boolean result = true;
        for (Package pack: getPackages().getValue()) {
            if(pack.getSelectedShiping() == null)
                return false;
        }
        return result;
    }

    public LiveData<Integer> getShippingPrice() {
        return mShippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.mShippingPrice.setValue(shippingPrice);
    }

    public void refreshShippingPrice() {
        int price = 0;
        for (Package pack : getPackages().getValue()) {
            price += pack.getPriceShipping();
        }
        setShippingPrice(price);
    }

    public LiveData<Integer> getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(int provinceId) {
        this.mProvinceId.setValue(provinceId);
    }

    public float getProductPrice(){
        float price = 0;
        for (Package pack: getPackages().getValue()) {
            for (Article art: pack.getArticleList()){
                price += art.getPrice();
            }
        }
        return price;
    }

    public String getPriceFormated() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(getProductPrice());
    }

    public String getTotalPriceFormated() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(getProductPrice() + mShippingPrice.getValue());
    }

    public void clean() {
        mPackages = new MutableLiveData<>();
        packageList = new ArrayList<>();
        mPackages.setValue(packageList);
        mShippingPrice = new MutableLiveData<>();
        mShippingPrice.setValue(0);
    }

    public List<Article> getPackages(int storeId) {
        for (Package pack: mPackages.getValue()) {
            if(pack.getStore().getId() == storeId)
                return pack.getArticleList();
        }
        return null;
    }
}