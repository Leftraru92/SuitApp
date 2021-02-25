package com.example.suitapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

public interface CaptureImageViewModel {
    void setImage(Bitmap bitmap, int requestId);

    void updateImage(Bitmap bitmap, int imageid);

    LiveData<Bitmap> getImage(int requestId, int imageId) ;
}
