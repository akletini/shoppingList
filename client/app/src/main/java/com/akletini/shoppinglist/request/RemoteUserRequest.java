package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.ui.login.LoginActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;
import com.akletini.shoppinglist.utils.HTTPUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteUserRequest {

    public static void remoteUserCreateRequest(final Context context, final String username) throws JSONException {

        final String url = SingletonRequestQueue.BASE_URL + "/login/getUserByUsername/" + username;

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final JsonObjectRequest request = new JsonObjectRequest(url, response -> {
            final UserDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), UserDto.class);
                LoggedInUserSingleton.getInstance().setCurrentUser(responseObject);
                final Intent intent;
                if (responseObject.getSignedIn() && responseObject.getStaySignedIn()) {
                    intent = new Intent(context, RouteHomeActivity.class);
                } else {
                    intent = new Intent(context, LoginActivity.class);
                }
                context.startActivity(intent);
            }
        }, error -> {
            final Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteUserExistsRequest(final Context context, final String username, final TextView textView) {

        final String url = SingletonRequestQueue.BASE_URL + "/login/userExists/" + username;

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final StringRequest request = new StringRequest(Request.Method.GET, url, response -> textView.setVisibility(View.INVISIBLE), error -> {
            Toast.makeText(context,
                    HTTPUtils.buildErrorFromHTTPResponse(error),
                    Toast.LENGTH_SHORT).show();
            textView.setVisibility(View.VISIBLE);
            textView.requestFocus();
        });

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
                    HTTPUtils.buildErrorFromHTTPResponse(error),
                    Toast.LENGTH_SHORT).show();
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

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
                final Intent intent = new Intent(context, RouteHomeActivity.class);
                context.startActivity(intent);
            }
        }, error -> Toast.makeText(context,
                HTTPUtils.buildErrorFromHTTPResponse(error),
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
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
