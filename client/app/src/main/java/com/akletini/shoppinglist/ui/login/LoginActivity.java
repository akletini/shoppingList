package com.akletini.shoppinglist.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MaterialButton loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            UserDto requestUserDto = new UserDto();
            String username = ViewUtils.textViewToString((TextView) findViewById(R.id.usernameEditText));
            String password = ViewUtils.textViewToString((TextView) findViewById(R.id.passwordEditText));
            Boolean staySignedIn = ViewUtils.checkBoxToBoolean((CheckBox) findViewById(R.id.staySignedInCheckbox));

            if (username.length() == 0) {
                Toast.makeText(LoginActivity.this, "Username must not be empty", Toast.LENGTH_LONG).show();
                // force input here
            } else if (password.length() == 0) {
                Toast.makeText(LoginActivity.this, "Password must not be empty", Toast.LENGTH_LONG).show();
            } else {
                requestUserDto.setUsername(username);
                requestUserDto.setPassword(password);
                requestUserDto.setStaySignedIn(staySignedIn);

                try {
                    RemoteUserRequest.remoteUserLoginRequest(LoginActivity.this, requestUserDto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        Intent intent = new Intent(this, CreateAccountActivity.class);
        TextView createAccountTextView = findViewById(R.id.registerAccount);
        createAccountTextView.setOnClickListener(view -> startActivity(intent));

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}