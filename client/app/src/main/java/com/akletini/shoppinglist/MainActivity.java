package com.akletini.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.request.RemoteItemRequest;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.ui.login.LoginActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("com.akletini.shoppinglist", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser", "");
        if (!username.isEmpty()) {
            // log user in
            try {
                RemoteUserRequest.remoteUserCreateRequest(MainActivity.this, username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            TextView helloWorld = findViewById(R.id.helloWorld);
            helloWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
        DataStoreRepository dataStoreRepository = DataStoreRepository.getInstance();
        try {
            RemoteItemRequest.remoteItemGetAllRequest(this, RouteHomeActivity.class, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}