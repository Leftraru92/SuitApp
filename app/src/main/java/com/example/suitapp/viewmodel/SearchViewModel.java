package com.example.suitapp.viewmodel;

import android.util.Size;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private final MutableLiveData<String> mSearchText;
    private MutableLiveData<Boolean> mIsStore;
    private MutableLiveData<String> mStoreName;
    private MutableLiveData<Category> mCategory;
    private MutableLiveData<Gender> mGenre;
    private MutableLiveData<Variant.Color> mColor;
    private MutableLiveData<Size> mSize;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mSearchText = new MutableLiveData<>();
        mIsStore = new MutableLiveData<>();
        mStoreName = new MutableLiveData<>();
        mIsStore.setValue(false);
        mCategory = new MutableLiveData<>();
        mGenre = new MutableLiveData<>();
        mColor = new MutableLiveData<>();
        mSize = new MutableLiveData<>();

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
        if(isStore)
            clean();
        mIsStore.setValue(isStore);
    }

    public String getStoreName() {
        return mStoreName.getValue();
    }

    public void setStoreName(String mStoreName) {
        this.mStoreName.setValue(mStoreName);
    }

    public LiveData<Category> getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        this.mCategory.setValue(category);
    }

    public LiveData<Gender> getGenre() {
        return mGenre;
    }

    public void setGenre(Gender genre) {
        this.mGenre.setValue(genre);
    }

    public LiveData<Variant.Color> getColor() {
        return mColor;
    }

    public void setColor(Variant.Color color) {
        this.mColor.setValue(color);
    }

    public LiveData<Size> getSize() {
        return mSize;
    }

    public void setSize(Size size) {
        this.mSize.setValue(size);
    }

    public void clean() {
        mSearchText.setValue("");
        mIsStore.setValue(false);
        mCategory.setValue(null);
        mGenre.setValue(null);
        mColor.setValue(null);
        mSize.setValue(null);
    }
}