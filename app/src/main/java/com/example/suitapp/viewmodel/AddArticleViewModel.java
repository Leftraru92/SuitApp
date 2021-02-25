package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;

import com.example.suitapp.dummy.DummyColors;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddArticleViewModel extends ViewModel implements CaptureImageViewModel, DialogSelectItemViewModel {

    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDesc;
    private MutableLiveData<Category> mCategory;
    private MutableLiveData<Gender> mGenre;
    private MutableLiveData<Float> mPrice;
    private MutableLiveData<ArrayList<Bitmap>> mImages;
    private MutableLiveData<Bitmap> mEditImage;
    private MutableLiveData<ArrayList<Variant>> mVariants;
    private MutableLiveData<Variant> mEditVariant;

    private ArrayList<Bitmap> listImages;
    private ArrayList<Variant> listVariants;

    public AddArticleViewModel() {
        mName = new MutableLiveData<>();
        mDesc = new MutableLiveData<>();
        mCategory = new MutableLiveData<>();
        mGenre = new MutableLiveData<>();
        mPrice = new MutableLiveData<>();
        mImages = new MutableLiveData<>();
        mEditImage = new MutableLiveData<>();
        mVariants = new MutableLiveData<>();
        mEditVariant = new MutableLiveData<>();

        listImages = new ArrayList<>();
        listVariants = new ArrayList<>();
        mImages.setValue(listImages);
        mVariants.setValue(listVariants);
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

    public LiveData<Float> getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        this.mPrice.setValue(price);
    }

    public LiveData<ArrayList<Bitmap>> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.mImages.setValue(images);
    }

    @Override
    public void setImage(Bitmap bitmap, int imageId) {

        listImages.add(bitmap);
        setImages(listImages);
    }

    @Override
    public void updateImage(Bitmap bitmap, int imageId) {
        listImages.remove(imageId);
        listImages.add(imageId, bitmap);
        mEditImage.setValue(null);
        setImages(listImages);
    }

    @Override
    public LiveData<Bitmap> getImage(int requestId, int imageId) {
        if (listImages.size() == 1)
            imageId = 0;
        mEditImage.setValue(listImages.get(imageId));
        return mEditImage;
    }

    public void deleteImage(int position) {
        listImages.remove(position);
        setImages(listImages);
    }

    public LiveData<ArrayList<Variant>> getVariants() {
        return mVariants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.mVariants.setValue(variants);
    }

    @Override
    public void selectValue(int id, String value, int requestId) {
        mEditVariant.setValue(new Variant(DummyColors.getColor(id), null, 0));
    }

    @Override
    public int getSelectValue(int requestId) {
        if (mEditVariant.getValue() != null)
            return mEditVariant.getValue().getColor().getId();
        else
            return 0;
    }

    public LiveData<Variant> getEditVariant() {
        return mEditVariant;
    }

    public void addVariant(String size, int qty) {
        //Si es editado primero lo elimino de la lista
        ArrayList<Variant> editedListVariants = listVariants;
        for (Variant var : listVariants) {
            if ((var.getColor().getId() == mEditVariant.getValue().getColor().getId()) && var.getSize().equals(size))
                editedListVariants.remove(var);
        }
        listVariants = editedListVariants;
        //Lo guardo en la lista
        mEditVariant.getValue().setSize(size);
        mEditVariant.getValue().setStock(qty);
        listVariants.add(mEditVariant.getValue());
        mVariants.setValue(listVariants);

        mEditVariant.setValue(null);
    }

    public void deleteVariant(int position) {
        listVariants.remove(position);
        mVariants.setValue(listVariants);
    }

    public void setEditVariant(int position) {
        if (position == -1)
            mEditVariant.setValue(null);
        else
            mEditVariant.setValue(listVariants.get(position));
    }
}