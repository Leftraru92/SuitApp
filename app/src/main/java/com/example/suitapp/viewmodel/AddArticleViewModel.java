package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddArticleViewModel extends ViewModel implements CaptureImageViewModel, DialogSelectItemViewModel {

    private MutableLiveData<Integer> mStoreId;
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
        mStoreId = new MutableLiveData<>();
        mName = new MutableLiveData<>();
        mDesc = new MutableLiveData<>();
        mCategory = new MutableLiveData<>();
        mGenre = new MutableLiveData<>();
        mPrice = new MutableLiveData<>();
        mImages = new MutableLiveData<>();
        mEditImage = new MutableLiveData<>();
        mVariants = new MutableLiveData<>();
        mEditVariant = new MutableLiveData<>();

        mStoreId.setValue(0);
        listImages = new ArrayList<>();
        listVariants = new ArrayList<>();
        mImages.setValue(listImages);
        mVariants.setValue(listVariants);
    }

    public LiveData<Integer> getStoreId() {
        return mStoreId;
    }

    public void setStoreId(int storeId) {
        this.mStoreId.setValue(storeId);
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

        if (requestId == 0)
            if (mEditVariant.getValue() == null)
                mEditVariant.setValue(new Variant(new Variant.Color(id, value), null, 0));
            else
                mEditVariant.setValue(new Variant(new Variant.Color(id, value), mEditVariant.getValue().getSize(), 0));
        else if (requestId == 1)
            if (mEditVariant.getValue() == null)
                mEditVariant.setValue(new Variant(null, new Variant.Size(id, value), 0));
            else
                mEditVariant.setValue(new Variant(mEditVariant.getValue().getColor(), new Variant.Size(id, value), 0));
    }

    @Override
    public int getSelectValue(int requestId) {
        if (requestId == 0)
            if (mEditVariant.getValue() != null && mEditVariant.getValue().getColor() != null)
                return mEditVariant.getValue().getColor().getId();
            else
                return 0;
        else if (requestId == 1)
            if (mEditVariant.getValue() != null && mEditVariant.getValue().getSize() != null)
                return mEditVariant.getValue().getSize().getId();
            else
                return 0;
        else return 0;
    }

    public LiveData<Variant> getEditVariant() {
        return mEditVariant;
    }

    public void addVariant(int qty) {
        //Si es editado primero lo elimino de la lista
        ArrayList<Variant> editedListVariants = new ArrayList<>();
        for (Variant var : listVariants) {
            if ((var.getColor().getId() == mEditVariant.getValue().getColor().getId()) && var.getSize().getId() == mEditVariant.getValue().getSize().getId())
                editedListVariants.add(var);
        }
        listVariants.removeAll(editedListVariants);
        //Lo guardo en la lista
        //mEditVariant.getValue().setSize(size);
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

    public JSONObject toJSON(String hash) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", hash);
            jsonObject.put("storeId", getStoreId().getValue());
            jsonObject.put("articleName", getName().getValue());
            jsonObject.put("articleDesc", getDesc().getValue());
            jsonObject.put("categoryId", getCategory().getValue().getId());
            jsonObject.put("genderId", getGenre().getValue().getId());
            jsonObject.put("articlePrice", getPrice().getValue());

            JSONArray jsonVariant = new JSONArray();
            for (Variant variant : getVariants().getValue()) {
                JSONObject jsonvar = new JSONObject();
                jsonvar.put("colorId", variant.getColor().getId());
                jsonvar.put("sizeId", variant.getSize().getId());
                jsonvar.put("stock", variant.getStock());
                jsonVariant.put(jsonvar);
            }
            jsonObject.put("variants", jsonVariant);

            JSONArray jsonImages = new JSONArray();
            for (Bitmap bitmap : getImages().getValue()) {
                jsonImages.put(Util.bitmapToBase64(bitmap));
            }
            jsonObject.put("images", jsonImages);

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}