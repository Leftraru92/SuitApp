package com.example.suitapp.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public static String getDateFormatted(String fecha, boolean hora) {

        SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat formateador;
        if (hora) {
            formateador = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        } else {
            formateador = new SimpleDateFormat("dd/MM/yyyy");
        }

        Date date = null;
        String dateFormat = null;
        if (!fecha.toString().equals("0001-01-01T00:00:00")) {
            try {
                date = parseador.parse(fecha);
                dateFormat = formateador.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                dateFormat = fecha;
            }
        }
        return dateFormat;
    }

    public static JSONObject createBody(HashMap<String, Object> bodyData){
        JSONObject body = null;
        try {
            body = new JSONObject();
            for (Map.Entry<String, Object> entry : bodyData.entrySet()) {
                body.put(entry.getKey(), entry.getValue());
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }
}
