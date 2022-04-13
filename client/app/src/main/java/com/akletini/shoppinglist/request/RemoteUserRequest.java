package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.akletini.shoppinglist.MainActivity;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteUserRequest {

    public static boolean remoteUserCreateRequest(Context context, String username) throws JSONException {
        final boolean[] success = {false};

        String url = SingletonRequestQueue.BASE_URL + "/login/getUserByUsername/" + username;

        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserDto responseObject;
                if (response != null) {
                    Gson gson = new Gson();
                    responseObject = gson.fromJson(response.toString(), UserDto.class);
                    LoggedInUserSingleton.getInstance().setCurrentUser(responseObject);
                    if (responseObject.getSignedIn()) {
                        Intent intent = new Intent(context, TripHomeActivity.class);
                        context.startActivity(intent);
                    }
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
}
