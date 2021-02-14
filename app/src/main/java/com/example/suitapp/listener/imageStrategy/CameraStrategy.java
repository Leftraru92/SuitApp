package com.example.suitapp.listener.imageStrategy;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.fragment.addStore.AddStoreViewModel;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.CaptureImageViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.content.FileProvider;

import static android.app.Activity.RESULT_CANCELED;
import static com.example.suitapp.listener.OclAddImage.MY_CAMERA_REQUEST_CODE;

public class CameraStrategy implements SelectImageStrategy {
    Context context;
    int requestCode;

    public CameraStrategy(Context context, int requestCode) {
        this.context = context;
        this.requestCode = requestCode;
    }

    @Override
    public boolean runIntent() {
        //Si saco foto y la captura de coordenadas esta activa obtengo xy
        Log.d(Constants.LOG, "Ejecuto camera strategy");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            takePhotoFromCamera();
        } else {
            if (context.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            } else {
                takePhotoFromCamera();
            }
        }
        return true;
    }

    public void takePhotoFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(Constants.LOG, "Error " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getString(R.string.fileprovider),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Constants.LAST_PHOTO_URI = photoFile.getAbsolutePath();
                Log.d(Constants.LOG, "Foto path " + Constants.LAST_PHOTO_URI);
                ((Activity) context).startActivityForResult(takePictureIntent, requestCode);

            } else {
                Toast.makeText(context, "Error al crear el archivo", Toast.LENGTH_LONG).show();
                //Snackbar.make(((Activity) context).findViewById(R.id.btnCamera), "Error al crear el archivo", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "No se encontró una aplicación compatible", Toast.LENGTH_LONG).show();
            //Snackbar.make(((Activity) context).findViewById(R.id.btnCamera), "No se encontró una aplicación compatible", Snackbar.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    public boolean processData(int requestCode, int resultCode, Intent data, CaptureImageViewModel mViewModel, int idImage) {
        Log.d(Constants.LOG, "Ejecuto camera processData");
        if (resultCode == RESULT_CANCELED)
            return false;

        String fotoPath = Constants.LAST_PHOTO_URI;
        Log.d(Constants.LOG, fotoPath);
        Bitmap bitmap = null;
        try {
            //bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(fotoPath));
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse("file://" + fotoPath)));

            //Escalo la imagen guardada
            File file = new File(fotoPath);

            try {
                FileOutputStream out = new FileOutputStream(file);
                //Rotate
                bitmap = (Bitmap) rotateImageIfRequired(context, bitmap, Uri.parse(fotoPath));
                //Resize
                bitmap = getResizedBitmap(bitmap, Constants.IMAGE_SIZE);
                //elimino imagen original
                if (file.exists())
                    file.delete();
                //Compress and save
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bitmap != null)
                mViewModel.setImage(bitmap, idImage);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(Constants.LOG, "Error " + e);
            Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation = getRotation(context, selectedImage);
        //int rotation = getOrientationFromExif(String.valueOf(selectedImage));
        if (Build.MANUFACTURER.equals("samsung")) {
            if (rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
                img.recycle();
                img = rotatedImg;
            }
        }
        return img;
    }

    private static int getRotation(Context context, Uri selectedImage) {

        Cursor cursor = context.getContentResolver().query(selectedImage,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return 90;  //Assuming it was taken portrait
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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