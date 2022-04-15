package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.ui.login.LoginActivity;
import com.akletini.shoppinglist.ui.trip.TripHomeActivity;
import com.akletini.shoppinglist.utils.ErrorUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginRequest {
    public static void remoteUserLoginRequest(final Context context, final UserDto userDto) throws JSONException {

        final String url = SingletonRequestQueue.BASE_URL + "/login/loginUser";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(userDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final UserDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), UserDto.class);
                LoggedInUserSingleton.getInstance().setCurrentUser(responseObject);
                final SharedPreferences sharedPreferences =
                        context.getSharedPreferences("com.akletini.shoppinglist", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("currentUser", responseObject.getUsername()).apply();
                final Intent intent = new Intent(context, TripHomeActivity.class);
                context.startActivity(intent);
            }
        }, error -> Toast.makeText(context,
                ErrorUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteUserCreateRequest(final Context context, final UserDto userDto) throws JSONException {

        final String url = SingletonRequestQueue.BASE_URL + "/login/createUser";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(userDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
        }, error -> Toast.makeText(context,
                ErrorUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteUserLogoutRequest(final Context context, final UserDto userDto) {

        final String url = SingletonRequestQueue.BASE_URL + "/login/logoutUser/" + userDto.getUsername();

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            LoggedInUserSingleton.getInstance().getCurrentUser().setSignedIn(false);
            final Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }, error -> {
            Toast.makeText(context,
                    ErrorUtils.buildErrorFromHTTPResponse(error),
                    Toast.LENGTH_SHORT).show();
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
