package com.akletini.shoppinglist.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
import com.akletini.shoppinglist.ui.itemlist.ItemListHomeActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;
import com.google.android.material.navigation.NavigationView;

public class MarketHomeActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_home);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.menuItemMarkets).setEnabled(false);
        setDrawerMenuUsername();
        setLogoutListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
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
            RemoteUserRequest.remoteUserLogoutRequest(MarketHomeActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
        });
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void switchToArticleEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, ItemHomeActivity.class));
    }

    public void switchToItemListEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, ItemListHomeActivity.class));
    }

    public void switchToMarketEditor(final MenuItem menuItem) {
    }

    public void switchToRouteEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, RouteHomeActivity.class));
    }
}