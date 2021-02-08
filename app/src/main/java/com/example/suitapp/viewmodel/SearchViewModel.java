package com.example.suitapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private final MutableLiveData<String> mSearchText;
    private MutableLiveData<Boolean> mIsStore;
    private MutableLiveData<String> mStoreName;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mSearchText = new MutableLiveData<>();
        mIsStore = new MutableLiveData<>();
        mStoreName = new MutableLiveData<>();
        mIsStore.setValue(false);
        mText.setValue("Este es el fragmento Buscar");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getSearchText() {
        return mSearchText;
    }

    public void setSearchText(String query) {
        mSearchText.setValue(query);
    }

    public MutableLiveData<Boolean> isStore() {
        return mIsStore;
    }

    public void setStore(boolean isStore){
        mIsStore.setValue(isStore);
    }

    public String getStoreName() {
        return mStoreName.getValue();
    }

    public void setStoreName(String mStoreName) {
        this.mStoreName.setValue(mStoreName);
    }
}