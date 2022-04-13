package com.akletini.shoppinglist.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequestQueue {

    private static SingletonRequestQueue instance;
    private Context context;
    private RequestQueue requestQueue;
    public static final String BASE_URL = "http://192.168.0.171:8080";

    private SingletonRequestQueue(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
}
