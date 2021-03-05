package com.example.suitapp.viewmodel;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> mOrder;
    private final MutableLiveData<String> mSearchText;
    private MutableLiveData<String> mStoreName;
    private MutableLiveData<Category> mCategory;
    private MutableLiveData<Gender> mGenre;
    private MutableLiveData<Variant.Color> mColor;
    private MutableLiveData<Integer> mMinPrice;
    private MutableLiveData<Integer> mMaxPrice;
    private MutableLiveData<Variant.Size> mSize;
    private MutableLiveData<Integer> mStore;
    private MutableLiveData<Boolean> mRefresh;
    //temp
    private MutableLiveData<Category> mCategoryTemp;
    private MutableLiveData<Gender> mGenreTemp;
    private MutableLiveData<Variant.Color> mColorTemp;
    private MutableLiveData<Variant.Size> mSizeTemp;
    private MutableLiveData<Integer> mMinPriceTemp;
    private MutableLiveData<Integer> mMaxPriceTemp;
    private int prevOrder;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mOrder = new MutableLiveData<>();
        mSearchText = new MutableLiveData<>();
        mStoreName = new MutableLiveData<>();
        mCategory = new MutableLiveData<>();
        mGenre = new MutableLiveData<>();
        mColor = new MutableLiveData<>();
        mMinPrice = new MutableLiveData<>();
        mMaxPrice = new MutableLiveData<>();
        mSize = new MutableLiveData<>();
        mStore = new MutableLiveData<>();
        mRefresh = new MutableLiveData<>();
        //temp
        mCategoryTemp = new MutableLiveData<>();
        mGenreTemp = new MutableLiveData<>();
        mColorTemp = new MutableLiveData<>();
        mSizeTemp = new MutableLiveData<>();
        mMinPriceTemp = new MutableLiveData<>();
        mMaxPriceTemp = new MutableLiveData<>();

        mOrder.setValue(1);
        prevOrder = 1;
        setStore(0);
        setRefresh(false);
        mText.setValue("");
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

    public LiveData<Integer> getMinPrice() {
        return mMinPrice;
    }

    public void setMinPrice(int minPrice) {
        mMinPrice.setValue(minPrice);
    }

    public LiveData<Integer> getMaxPrice() {
        return mMaxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.mMaxPrice.setValue(maxPrice);
    }

    public MutableLiveData<Variant.Size> getSize() {
        return mSize;
    }

    public void setSize(Variant.Size size) {
        this.mSize.setValue(size);
    }

    public LiveData<Integer> getStore() {
        return mStore;
    }

    public void setStore(int store) {
        this.mStore.setValue(store);
    }

    public LiveData<Boolean> getRefresh() {
        return mRefresh;
    }

    public void setRefresh(boolean refresh) {
        this.mRefresh.setValue(refresh);
    }

    public LiveData<Category> getCategoryTemp() {
        return mCategoryTemp;
    }

    public void setCategoryTemp(Category categoryTemp) {
        this.mCategoryTemp.setValue(categoryTemp);
    }

    public LiveData<Gender> getGenreTemp() {
        return mGenreTemp;
    }

    public void setGenreTemp(Gender genreTemp) {
        this.mGenreTemp.setValue(genreTemp);
    }

    public LiveData<Variant.Color> getColorTemp() {
        return mColorTemp;
    }

    public void setColorTemp(Variant.Color colorTemp) {
        this.mColorTemp.setValue(colorTemp);
    }

    public LiveData<Variant.Size> getSizeTemp() {
        return mSizeTemp;
    }

    public void setSizeTemp(Variant.Size sizeTemp) {
        this.mSizeTemp.setValue(sizeTemp);
    }

    public LiveData<Integer> getOrder() {
        return mOrder;
    }

    public String getOrderString() {
        switch (mOrder.getValue()) {
            case 1:
                return "Más relevante";
            case 2:
                return "Mayor precio";
            case 3:
                return "Menor precio";
            default:
                return "";
        }
    }

    public void setOrder(int order) {
        prevOrder = mOrder.getValue();
        this.mOrder.setValue(order);
    }

    public LiveData<Integer> getMinPriceTemp() {
        return mMinPriceTemp;
    }

    public void setMinPriceTemp(String minPriceTemp) {
        try {
            int val = Integer.valueOf(minPriceTemp);
            this.mMinPriceTemp.setValue(val);
        } catch (Exception ex) {
            this.mMinPriceTemp.setValue(null);
        }
    }

    public LiveData<Integer> getMaxPriceTemp() {
        return mMaxPriceTemp;
    }

    public void setMaxPriceTemp(String maxPriceTemp) {
        try {
            int val = Integer.valueOf(maxPriceTemp);
            this.mMaxPriceTemp.setValue(val);
        } catch (Exception ex) {
            this.mMaxPriceTemp.setValue(null);
        }
    }

    public void clean() {
        //mSearchText.setValue("");
        mCategory.setValue(null);
        mGenre.setValue(null);
        mColor.setValue(null);
        mSize.setValue(null);
        mMinPrice.setValue(null);
        mMaxPrice.setValue(null);
        //mStore.setValue(0);
        mOrder.setValue(1);
    }

    public void cleanTemp() {
        mCategoryTemp.setValue(null);
        mGenreTemp.setValue(null);
        mColorTemp.setValue(null);
        mSizeTemp.setValue(null);
        mOrder.setValue(prevOrder);
        mMaxPriceTemp.setValue(null);
        mMaxPriceTemp.setValue(null);
    }

    public void setFilter() {
        mCategory.setValue(mCategoryTemp.getValue());
        mGenre.setValue(mGenreTemp.getValue());
        mColor.setValue(mColorTemp.getValue());
        mSize.setValue(mSizeTemp.getValue());
        mMaxPrice.setValue(mMaxPriceTemp.getValue());
        mMinPrice.setValue(mMinPriceTemp.getValue());
        int order = mOrder.getValue();
        cleanTemp();
        mOrder.setValue(order);
        setRefresh(!mRefresh.getValue());
    }

    public void setFilterTemp() {
        mCategoryTemp.setValue(mCategory.getValue());
        mGenreTemp.setValue(mGenre.getValue());
        mColorTemp.setValue(mColor.getValue());
        mSizeTemp.setValue(mSize.getValue());
        mMaxPriceTemp.setValue(mMaxPrice.getValue());
        mMinPriceTemp.setValue(mMinPrice.getValue());
    }

    public String getFilterCount(){
        int i = 0;
        if(mCategory.getValue()!=null && mCategory.getValue().getId() != 0)
            i++;
        if(mGenre.getValue()!=null && mGenre.getValue().getId() != 0)
            i++;
        if(mColor.getValue()!=null && mColor.getValue().getId() != 0)
            i++;
        if(mSize.getValue()!=null && mSize.getValue().getId() != 0)
            i++;
        if(mMinPrice.getValue()!=null && mMinPrice.getValue() != 0)
            i++;
        if(mMaxPrice.getValue()!=null && mMaxPrice.getValue() != 0)
            i++;
        return (i==0) ? "" : " (" + i + ")";
    }
}