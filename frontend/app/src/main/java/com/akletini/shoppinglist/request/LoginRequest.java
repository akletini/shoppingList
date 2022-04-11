package com.akletini.shoppinglist.request;

import android.content.Context;
import android.widget.Toast;

import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.ui.LoginActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginRequest {
    public static void remoteUserLoginRequest(Context context, UserDto userDto) throws JSONException {
        String url = SingletonRequestQueue.BASE_URL + "/login/saveUser";

        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        String jsonObject = new Gson().toJson(userDto);
        JSONObject requestObject = new JSONObject(jsonObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Request returned", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
