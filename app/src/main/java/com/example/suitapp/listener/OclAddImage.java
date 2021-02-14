package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.suitapp.R;
import com.example.suitapp.fragment.addStore.AddStoreViewModel;
import com.example.suitapp.listener.imageStrategy.CameraStrategy;
import com.example.suitapp.listener.imageStrategy.GalleryStrategy;
import com.example.suitapp.listener.imageStrategy.SelectImageStrategy;
import com.example.suitapp.viewmodel.CaptureImageViewModel;

import androidx.appcompat.app.AlertDialog;

public class OclAddImage implements View.OnClickListener {

    private SelectImageStrategy strategy;
    static Context context;
    int requestCode, idImage;
    public static final int MY_CAMERA_REQUEST_CODE = 100;
    boolean allowMultiple;
    CaptureImageViewModel mViewModel;

    public OclAddImage(Context context, int requestCode, boolean allowMultiple, CaptureImageViewModel mViewModel, int idImage) {
        this.context = context;
        this.requestCode = requestCode;
        this.allowMultiple = allowMultiple;
        this.mViewModel = mViewModel;
        this.idImage = idImage;
    }

    private void setStrategy(SelectImageStrategy strategy) {
        this.strategy = strategy;
    }

    private boolean runIntent() {
        return strategy.runIntent();
    }

    public boolean processData(int requestCode, int resultCode, Intent data) {
        return strategy.processData(requestCode, resultCode, data, mViewModel ,idImage);
    }

    @Override
    public void onClick(View view) {

        if (!((Activity) context).isFinishing())
            showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle(context.getString(R.string.selec_option_img));
        String[] pictureDialogItems = {
                context.getString(R.string.selec_galery),
                context.getString(R.string.capture_photo)};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            setStrategy(new GalleryStrategy(context, requestCode, allowMultiple));
                            runIntent();
                            break;
                        case 1:
                            setStrategy(new CameraStrategy(context, requestCode));
                            runIntent();
                    }
                });
        pictureDialog.show();
    }
}
