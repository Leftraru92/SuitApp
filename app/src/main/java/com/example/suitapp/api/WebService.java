package com.example.suitapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.suitapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WebService {
    Context context;
    String url;
    String params;
    Constants.JSON_TYPE typeResponse;
    int reintento = 0;
    int method, requestCode;
    JSONObject jsonBody;
    CallWebService origin;

    public WebService(Context context, int requestCode) {
        this.context = context;
        this.requestCode = requestCode;
    }

    public void callService(CallWebService origin, String url, String params, int method, Constants.JSON_TYPE typeResponse, JSONObject jsonBody) {
        this.origin = origin;
        this.url = url;
        this.params = params;
        this.method = method;
        this.typeResponse = typeResponse;
        this.jsonBody = jsonBody;

        Log.d(Constants.LOG, url);

        if (jsonBody != null)
            Log.d(Constants.LOG, "Body: " + jsonBody.toString());

        trabajoOnline();
/*
        //Trabajo offline?
        boolean offline = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("swTrabajarOffline", false);
        if (offline && Arrays.asList(Constantes.WS_FUNCIONA_OFFLINE).contains(partialUrl)) {
            if (!comprobarBaseOffline(offline)) {
                Log.i(Constantes.LOG_NAME, "No se encontraron datos offline: ");
                Log.i(Constantes.LOG_NAME, "Buscando Online...: ");
                trabajoOnline();
            }

        } else {
            trabajoOnline();
        }
*/
    }

    private void trabajoOnline() {

        RequestQueue queue = Volley.newRequestQueue(context);
        DefaultRetryPolicy policy = new DefaultRetryPolicy(
                Constants.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        if(params != null)
            url = url + params;

        switch (typeResponse) {
            case OBJECT:
                JsonObjectRequest stringRequestPost = new JsonObjectRequest(method, url, jsonBody,
                        jsonObjectListener(url), newErrorListener(url)) {
/*
                    @Override
                    public Map<String, String> getHeaders() {
                        return getToken(context);
                    }*/
                };
                stringRequestPost.setRetryPolicy(policy);
                queue.add(stringRequestPost);

                break;
            case ARRAY:
                JsonArrayRequest stringRequest = new JsonArrayRequest(method, url, null,
                        jsonArrayListener(url), newErrorListener(url)) {
/*
                    @Override
                    public Map<String, String> getHeaders() {
                        return getToken(context);
                    }*/
                };
                stringRequest.setRetryPolicy(policy);
                queue.add(stringRequest);
                break;
        }
    }


    private Response.Listener<JSONObject> jsonObjectListener(String url) {
        return
                response -> {
                    Log.i(Constants.LOG, "Respuesta " + url + ": " + response.toString());
                    origin.onResult(requestCode, response);
                };
    }

    private Response.Listener<JSONArray> jsonArrayListener(String url) {
        return
                response -> {
                    Log.i(Constants.LOG, "Respuesta " + url + ": " + response.toString());
                    origin.onResult(requestCode, response);
                };
    }

    private Response.ErrorListener newErrorListener(String url) {

        return
                (Response.ErrorListener) error -> {
                    error.printStackTrace();
                    String description = "";
                    String message;

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.i(Constants.LOG, error.toString());
                        origin.onError(requestCode, "No se pudo conectar al servidor");
                    } else {
                        try {
                            NetworkResponse networkResponse = error.networkResponse;
                            final String result = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
                            Log.i(Constants.LOG, url + " " + result);

                            JSONObject descriptionjson = null;
                            if (!result.equals("") && !result.equals("\"\"")) {
                                descriptionjson = new JSONObject(result);
                            }

                            //Obtengo mensaje de error
                            switch (networkResponse.statusCode) {
                                case 401:
                                    description = "Se ha denegado la autorización para esta solicitud.";
                                    break;
                                case 403:
                                    description = descriptionjson.getString("Message");
                                    break;
                                case 404:
                                    description = "No encontrado";
                                    break;
                                default:
                                    try {
                                        description = descriptionjson.getString("Message");
                                    } catch (Exception e) {
                                        try {
                                            description = descriptionjson.getString("ExceptionMessage");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            description = "Error";
                                        }
                                    }
                                    break;
                            }

                            //Muestro mensaje de error
                            Log.i(Constants.LOG, url + " " + networkResponse.statusCode + " " + description);
                            origin.onError(requestCode, description);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.i(Constants.LOG, e.getMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(Constants.LOG, e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(Constants.LOG, e.getMessage());
                        }
                    }
                };
    }

    /*private void getNuevoToken() {

        Log.i(Constantes.LOG_NAME, "Cierro ");
        String[] token = Util.getTokenId(context);

    }

    private Map<String, String> getToken(Context context) {
        Map<String, String> params = new HashMap<String, String>();

        String token = context.getSharedPreferences("_", MODE_PRIVATE).getString("TOKEN", "");
        Log.d(Constantes.LOG_NAME, "token: " + token);
        params.put("Authorization", "jwt " + token);
        //params.put("Authorization", "bearer " + Constantes.TOKEN);
        return params;
    }*/
}
