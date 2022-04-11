package com.akletini.shoppinglist.utils;

import android.widget.CheckBox;
import android.widget.TextView;

public class ViewUtils {

    private ViewUtils() {}

    public static String textViewToString(TextView textView) {
        return textView.getText().toString();
    }

    public static Boolean checkBoxToBoolean(CheckBox checkBox) {
        return checkBox.isChecked();
    }
}
