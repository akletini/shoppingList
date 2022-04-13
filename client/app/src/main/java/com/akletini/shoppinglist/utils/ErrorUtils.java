package com.akletini.shoppinglist.utils;

import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;

public class ErrorUtils {

    private ErrorUtils() {}

    public static String buildErrorFromHTTPResponse(VolleyError error) {
        String body = "";
        //get status code here
        String statusCode = String.valueOf(error.networkResponse.statusCode);
        //get response body and parse with appropriate encoding
        if(error.networkResponse.data != null) {
            try {
                body = new String(error.networkResponse.data,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "Error " + statusCode + ": " + body;
    }
}
