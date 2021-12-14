package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.suitapp.model.Article;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSession;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticleDetailViewModel extends ViewModel implements CaptureImageViewModel {

    private MutableLiveData<Integer> mArticleId;
    private MutableLiveData<Article> mArticle;
    private MutableLiveData<String> mName;
    private MutableLiveData<Integer> mQty;
    private MutableLiveData<String> mSize;
    private MutableLiveData<String> mColor;
    private MutableLiveData<Integer> mStock;
    private MutableLiveData<List<String>> mImagesString;

    public ArticleDetailViewModel() {
        mArticleId = new MutableLiveData<>();
        mArticle = new MutableLiveData<>();
        mName = new MutableLiveData<>();
        mQty = new MutableLiveData<>();
        mSize = new MutableLiveData<>();
        mColor = new MutableLiveData<>();
        mStock = new MutableLiveData<>();
        mImagesString = new MutableLiveData<>();

        mQty.setValue(1);
    }

    public LiveData<Integer> getArticleId() {
        return mArticleId;
    }

    public void setArticleId(int articleId) {
        if(this.mArticleId.getValue() != null)
            cleanOptions();
        this.mArticleId.setValue(articleId);

    }

    private void cleanOptions() {
        mSize.setValue(null);
        mColor.setValue(null);
    }

    public LiveData<Article> getArticle() {
        return mArticle;
    }

    public void setArticle(Article article) {
        this.mArticle.setValue(article);
    }

    public MutableLiveData<String> getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName.setValue(name);
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

    public LiveData<Integer> getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        this.mStock.setValue(stock);
    }

    public void setImagesString(List<String> lImagenes) {
        mImagesString.setValue(lImagenes);
    }

    public List<String> getImagesString() {
        return mImagesString.getValue();
    }

    @Override
    public void setImage(Bitmap bitmap, int requestId) {

    }

    @Override
    public void updateImage(Bitmap bitmap, int imageid) {

    }

    @Override
    public LiveData<Bitmap> getImage(int requestId, int imageId) {
        String image = getImagesString().get(imageId);
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        MutableLiveData<Bitmap> bitmapLiveData = new MutableLiveData<>();
        bitmapLiveData.setValue(decodedByte);
        return bitmapLiveData;
    }
}