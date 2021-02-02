package com.example.suitapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private final MutableLiveData<String> searchText;

    private final MutableLiveData<String> selected = new MutableLiveData<String>();

    public void select(String item) {
        selected.setValue(item);
    }

    public LiveData<String> getSelected() {
        return selected;
    }

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        searchText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento Buscar");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(String query) {
        searchText.setValue(query);
    }
}