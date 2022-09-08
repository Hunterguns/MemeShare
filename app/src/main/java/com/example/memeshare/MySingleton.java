package com.example.memeshare;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton instance;
    private static Context mContext;
    private RequestQueue requestQueue;

    public MySingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext);
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if(instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }

    public void addToRequestQueue(JsonObjectRequest request){
        requestQueue.add(request);
    }
}
