package com.example.suitapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticleDetailViewModel extends ViewModel {

    private MutableLiveData<Integer> mQty;
    private MutableLiveData<String> mSize;
    private MutableLiveData<String> mColor;

    public ArticleDetailViewModel() {
        mQty = new MutableLiveData<>();
        mSize = new MutableLiveData<>();
        mColor = new MutableLiveData<>();
    }

    public LiveData<Integer> getQty() {
        return mQty;
    }

    public void setQty(int mQty) {
        this.mQty.setValue(mQty);
    }

    public LiveData<String> getSize() {
        return mSize;
    }

    public void setSize(String mSize) {
        this.mSize.setValue(mSize);
    }

    public LiveData<String> getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor.setValue(mColor);
    }
}