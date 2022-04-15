package com.akletini.shoppinglist.ui.trip;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.request.LoginRequest;
import com.google.android.material.navigation.NavigationView;

public class TripHomeActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_home);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        setDrawerMenuUsername();
        setLogoutListener();
    }

    public void setDrawerMenuUsername() {
        NavigationView navView = findViewById(R.id.navigation_view);
        Menu menu = navView.getMenu();
        MenuItem item = menu.findItem(R.id.menuUsername);
        final String username = LoggedInUserSingleton.getInstance().getCurrentUser().getUsername();
        item.setTitle(username);
    }

    public void setLogoutListener() {
        final TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            LoginRequest.remoteUserLogoutRequest(TripHomeActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
        });
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}