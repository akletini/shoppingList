package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.ui.trip.TripHomeActivity;
import com.akletini.shoppinglist.utils.ErrorUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginRequest {
    public static boolean remoteUserLoginRequest(Context context, UserDto userDto) throws JSONException {
        final boolean[] success = {false};

        String url = SingletonRequestQueue.BASE_URL + "/login/loginUser";

        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        String jsonObject = new Gson().toJson(userDto);
        JSONObject requestObject = new JSONObject(jsonObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserDto responseObject = new UserDto();
                if (response != null) {
                    Gson gson = new Gson();
                    responseObject = gson.fromJson(response.toString(), UserDto.class);
                    LoggedInUserSingleton.getInstance().setCurrentUser(responseObject);
                    SharedPreferences sharedPreferences =
                            context.getSharedPreferences("com.akletini.shoppinglist", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("currentUser", responseObject.getUsername()).apply();
                    Intent intent = new Intent(context, TripHomeActivity.class);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        ErrorUtils.buildErrorFromHTTPResponse(error),
                        Toast.LENGTH_SHORT).show();
                success[0] = false;
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return success[0];
    }

    public static boolean remoteUserCreateRequest(Context context, UserDto userDto) throws JSONException {
        final boolean[] success = {false};

        String url = SingletonRequestQueue.BASE_URL + "/login/createUser";

        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        String jsonObject = new Gson().toJson(userDto);
        JSONObject requestObject = new JSONObject(jsonObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                Toast.makeText(context, "Request returned", Toast.LENGTH_SHORT).show();
                success[0] = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        ErrorUtils.buildErrorFromHTTPResponse(error),
                        Toast.LENGTH_SHORT).show();
                success[0] = false;
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return success[0];
    }

    public static synchronized boolean remoteUserExistsRequest(Context context, String username, TextView textView) {
        final boolean[] success = {false};

        String url = SingletonRequestQueue.BASE_URL + "/login/userExists/" + username;

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.INVISIBLE);
                success[0] = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        ErrorUtils.buildErrorFromHTTPResponse(error),
                        Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.VISIBLE);
                textView.requestFocus();
                success[0] = true;
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

        return success[0];
    }
}
