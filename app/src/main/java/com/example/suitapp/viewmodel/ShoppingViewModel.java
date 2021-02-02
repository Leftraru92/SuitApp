package com.example.suitapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShoppingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento Compras");
    }

    public LiveData<String> getText() {
        return mText;
    }
}