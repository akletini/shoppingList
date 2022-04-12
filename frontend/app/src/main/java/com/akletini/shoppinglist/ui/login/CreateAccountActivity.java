package com.akletini.shoppinglist.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.request.LoginRequest;
import com.akletini.shoppinglist.utils.ValidationUtils;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText emailEditText = findViewById(R.id.emailEditText);
        TextView wrongEmailFormat = findViewById(R.id.wrongEmailFormat);

        emailEditText.setOnFocusChangeListener((view, b) -> {
            if (emailEditText.getText().length() > 0 && !ValidationUtils.validateEmail(emailEditText.getText().toString())) {
                wrongEmailFormat.setVisibility(View.VISIBLE);
            } else {
                wrongEmailFormat.setVisibility(View.INVISIBLE);
            }
        });
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        TextView usernameAlreadyTaken = findViewById(R.id.usernameAlreadyTaken);
        usernameEditText.setOnFocusChangeListener((view, b) -> {
            if (usernameEditText.getText().length() > 0) {
                LoginRequest.remoteUserExistsRequest(getApplicationContext(), ViewUtils.textViewToString(usernameEditText), usernameAlreadyTaken);
            }
        });
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        TextView passwordsNotMatching = findViewById(R.id.passwordsDifferent);
        repeatPasswordEditText.setOnFocusChangeListener((view, b) -> {
            if (repeatPasswordEditText.getText().length() > 0 && !repeatPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                passwordsNotMatching.setVisibility(View.VISIBLE);
            } else {
                passwordsNotMatching.setVisibility(View.INVISIBLE);
            }
        });


        MaterialButton signUpButton = findViewById(R.id.loginButton);
        signUpButton.setOnClickListener(view -> {
            boolean noErrors = wrongEmailFormat.getVisibility() == View.INVISIBLE
                    && passwordsNotMatching.getVisibility() == View.INVISIBLE
                    && usernameAlreadyTaken.getVisibility() == View.INVISIBLE;

            boolean fieldsEmpty = ViewUtils.textViewToString(usernameEditText).isEmpty()
                    && ViewUtils.textViewToString(emailEditText).isEmpty()
                    && ViewUtils.textViewToString(passwordEditText).isEmpty()
                    && ViewUtils.textViewToString(repeatPasswordEditText).isEmpty();

            if (noErrors && !fieldsEmpty) {
                UserDto requestUserDto = new UserDto();
                requestUserDto.setUsername(ViewUtils.textViewToString(usernameEditText));
                requestUserDto.setEmail(ViewUtils.textViewToString(emailEditText));
                requestUserDto.setStaySignedIn(true);
                requestUserDto.setPassword(ViewUtils.textViewToString(repeatPasswordEditText));

                try {
                    LoginRequest.remoteUserCreateRequest(getApplicationContext(), requestUserDto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please fill out the fields", Toast.LENGTH_SHORT).show();
            }
        });

    }
}