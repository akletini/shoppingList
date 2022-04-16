package com.akletini.shoppinglist.utils;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HTTPUtils {

    private HTTPUtils() {
    }

    public static String buildErrorFromHTTPResponse(VolleyError error) {
        String body = "";
        //get status code here
        if (error.networkResponse == null) {
            return "Could not get a response from the server";
        }
        String statusCode = String.valueOf(error.networkResponse.statusCode);
        //get response body and parse with appropriate encoding
        if (error.networkResponse.data != null) {
            try {
                body = new String(error.networkResponse.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "Error " + statusCode + ": " + body;
    }

    public static List<JSONObject> jsonArrayToJsonObject(JSONArray array) {
        List<JSONObject> objects = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                objects.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }
}
