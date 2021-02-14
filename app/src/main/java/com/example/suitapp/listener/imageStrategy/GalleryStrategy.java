package com.example.suitapp.listener.imageStrategy;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.fragment.addStore.AddStoreViewModel;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.CaptureImageViewModel;

import java.io.IOException;
import java.net.URI;

import androidx.lifecycle.ViewModel;

import static android.app.Activity.RESULT_CANCELED;

public class GalleryStrategy implements SelectImageStrategy{
    Context context;
    int requestCode;
    boolean allowMultiple;

    public GalleryStrategy(Context context, int requestCode, boolean allowMultiple) {
        this.context = context;
        this.requestCode = requestCode;
        this.allowMultiple = allowMultiple;
    }

    @Override
    public boolean runIntent() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
        try {
            ((Activity) context).startActivityForResult(galleryIntent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No se encontró una aplicación compatible", Toast.LENGTH_SHORT).show();
           // Snackbar.make(((Activity) context).findViewById(R.id.btnCamera), "No se encontró una aplicación compatible", Snackbar.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean processData(int requestCode, int resultCode, Intent data, CaptureImageViewModel mViewModel, int idImage) {
        Log.d(Constants.LOG, "Ejecuto gallery processData");
        if (resultCode == RESULT_CANCELED) {
            return false;
        }

        if (data != null) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount() ;
                /*int count = data.getClipData().getItemCount() + llImagenes.getChildCount();
                if (count > cantImagenes)
                    count = cantImagenes - llImagenes.getChildCount();

                if (count > data.getClipData().getItemCount()) {
                    count = data.getClipData().getItemCount();
                }*/

                for (int i = 0; i < count; i++) {
                    Uri contentURI = data.getClipData().getItemAt(i).getUri();
                    procesBitmap(contentURI, mViewModel, idImage);
                }
            } else if (data.getData() != null) {
                Uri contentURI = data.getData();
                procesBitmap(contentURI, mViewModel, idImage);
            }
        }
        return true;
    }

    private void procesBitmap(Uri contentURI, CaptureImageViewModel mViewModel, int idImage){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
            bitmap = getResizedBitmap(bitmap, Constants.IMAGE_SIZE);
            mViewModel.setImage(bitmap, idImage);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
