package com.example.suitapp.util;

import com.google.android.gms.maps.model.LatLng;

public class Constants {

    public static final String WEB_CLIENT = "396255124955-8422d53koteuttt1n8bfi3q06upsrube.apps.googleusercontent.com";
    public static final String SERVICE_ID = "AAAAXEKlXds:APA91bFyck6eC2T_MAxOKTqZx-3Hi7OdRzh-V1RaV__pV6Bxnb_58z5brzT_mPo6z0MnQt5QyS2NfsjAMEpsWYuL0TXwzLFmngss5wE2kMIdS2n99tcmyZ7q59B1K94rTr7YldqPKmDN";
    public static final int DEFAULT_TIMEOUT = 30000;

    public static final String WS_DOMINIO = "http://suitapp-ws-apirest.us-west-2.elasticbeanstalk.com";
    public static final String WS_CATEGORIES = "/api/Categorias";
    public static final String WS_GENDERS = "/api/Genders";
    public static final String WS_ARTICLES = "/api/Articles";
    public static final String WS_IMAGES = "/api/Images";
    public static final String WS_STORES = "/api/Stores";
    public static final String WS_COLORS = "/api/Colours";
    public static final String WS_SIZES = "/api/Sizes";
    public static final String WS_LOGIN = "/api/Login";
    public static final String WS_FAV = "/api/Favorites";
    public static final String WS_CART = "/api/Cart";
    public static final String WS_ADDRESS = "/api/Address";
    public static final String WS_SHOPPING = "/api/Shopping";
    public static final String WS_COMMENTS = "/api/Questions";

    public static String LOG = "SuitApp";
    public static String LAST_PHOTO_URI ="";
    public static final String IMAGE_DIRECTORY = "/SUITAPP";
    public static final int IMAGE_SIZE = 400;
    public static final String TODOS = "Todos";

    public static final int IMAGE_PORTADA = 1;
    public static final int IMAGE_LOGO = 2;
    public static final int IMAGE_ARTICLE = 3;
    public static final int IMAGE_ARTICLE_DETAIL = 4;
    public static final int SELECT_PROVINCE = 1;
    public static final int SELECT_SHIPPING = 2;

    public enum ACTION {CREATE, UPDATE, DELETE};
    public enum TYPE {ARTICLE, STORE};
    public enum JSON_TYPE {OBJECT, ARRAY};
    public static final int JSON_OBJECT = 1;
    public static final int JSON_ARRAY = 2;

    public static LatLng ARGENTINA_LATLONG = new LatLng(-33.982860, -60.090262);
}
