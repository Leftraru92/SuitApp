package com.example.suitapp.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Util {

    public static int getPxByDensity(Resources resources, int dp) {
        final float scale = resources.getDisplayMetrics().density;
        int padding_in_px = (int) (dp * scale + 0.5f);
        return padding_in_px;
    }

    public static String bitmapToBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.d(Constantes.LOG_NAME, "Imagen Base64: " + encImage);

        return encImage;
    }

    public static Bitmap base64ToBitmap(String image){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
