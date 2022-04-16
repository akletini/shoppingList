package com.akletini.shoppinglist.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.utils.ValidationUtils;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final EditText emailEditText = findViewById(R.id.emailEditText);
        final TextView wrongEmailFormat = findViewById(R.id.wrongEmailFormat);

        emailEditText.setOnFocusChangeListener((view, b) -> {
            if (emailEditText.getText().length() > 0 && !ValidationUtils.validateEmail(emailEditText.getText().toString())) {
                wrongEmailFormat.setVisibility(View.VISIBLE);
            } else {
                wrongEmailFormat.setVisibility(View.INVISIBLE);
            }
        });
        final EditText usernameEditText = findViewById(R.id.usernameEditText);
        final TextView usernameAlreadyTaken = findViewById(R.id.usernameAlreadyTaken);
        usernameEditText.setOnFocusChangeListener((view, b) -> {
            if (usernameEditText.getText().length() > 0) {
                RemoteUserRequest.remoteUserExistsRequest(getApplicationContext(), ViewUtils.textViewToString(usernameEditText), usernameAlreadyTaken);
            }
        });
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final EditText repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        final TextView passwordsNotMatching = findViewById(R.id.passwordsDifferent);
        repeatPasswordEditText.setOnFocusChangeListener((view, b) -> {
            if (repeatPasswordEditText.getText().length() > 0 && !repeatPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                passwordsNotMatching.setVisibility(View.VISIBLE);
            } else {
                passwordsNotMatching.setVisibility(View.INVISIBLE);
            }
        });


        final MaterialButton signUpButton = findViewById(R.id.loginButton);
        signUpButton.setOnClickListener(view -> {
            final boolean noErrors = wrongEmailFormat.getVisibility() == View.INVISIBLE
                    && passwordsNotMatching.getVisibility() == View.INVISIBLE
                    && usernameAlreadyTaken.getVisibility() == View.INVISIBLE;

            final boolean fieldsEmpty = ViewUtils.textViewToString(usernameEditText).isEmpty()
                    && ViewUtils.textViewToString(emailEditText).isEmpty()
                    && ViewUtils.textViewToString(passwordEditText).isEmpty()
                    && ViewUtils.textViewToString(repeatPasswordEditText).isEmpty();

            if (noErrors && !fieldsEmpty) {
                final UserDto requestUserDto = new UserDto();
                requestUserDto.setUsername(ViewUtils.textViewToString(usernameEditText));
                requestUserDto.setEmail(ViewUtils.textViewToString(emailEditText));
                requestUserDto.setStaySignedIn(false);
                requestUserDto.setPassword(ViewUtils.textViewToString(repeatPasswordEditText));

                try {
                    RemoteUserRequest.remoteUserCreateRequest(getApplicationContext(), requestUserDto);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please fill out the fields", Toast.LENGTH_SHORT).show();
            }
        });

    }
}