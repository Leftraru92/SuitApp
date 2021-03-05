package com.example.suitapp.viewmodel;

import com.example.suitapp.model.Package;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingViewModel extends ViewModel {

    private MutableLiveData<List<Package>> mPackages;
    private List<Package> packageList;

    public ShoppingViewModel() {
        mPackages = new MutableLiveData<>();
        packageList = new ArrayList<>();
        mPackages.setValue(packageList);
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
}