package com.akletini.shoppinglist.ui;

import android.os.Bundle;

import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.databinding.ActivityLoginBinding;
import com.akletini.shoppinglist.request.LoginRequest;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.akletini.shoppinglist.R;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        MaterialButton loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDto requestUserDto = new UserDto();
                String username = ViewUtils.textViewToString((TextView) findViewById(R.id.usernameEditText));
                String password = ViewUtils.textViewToString((TextView) findViewById(R.id.passwordEditText));
                Boolean staySignedIn = ViewUtils.checkBoxToBoolean((CheckBox) findViewById(R.id.staySignedInCheckbox));

                if (username.length() == 0) {
                    Toast.makeText(LoginActivity.this,"Username must not be empty", Toast.LENGTH_LONG).show();
                    // force input here
                } else if (password.length() == 0) {
                    Toast.makeText(LoginActivity.this,"Password must not be empty", Toast.LENGTH_LONG).show();
                } else {
                    requestUserDto.setUsername(username);
                    requestUserDto.setPassword(password);
                    requestUserDto.setStaySignedIn(staySignedIn);

                    try {
                        LoginRequest.remoteUserLoginRequest(getApplicationContext(), requestUserDto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }



}