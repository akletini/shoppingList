package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
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
                    intent = new Intent(context, TripHomeActivity.class);
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
                    ErrorUtils.buildErrorFromHTTPResponse(error),
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
}
