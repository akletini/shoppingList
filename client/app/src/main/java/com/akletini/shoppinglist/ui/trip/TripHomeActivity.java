package com.akletini.shoppinglist.ui.trip;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.RouteDto;
import com.akletini.shoppinglist.request.LoginRequest;
import com.akletini.shoppinglist.utils.TestUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class TripHomeActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        FloatingActionButton addRouteButton = findViewById(R.id.addRouteButton);

        //TODO: Only for testing, remove after
        List<RouteDto> testRoutes = TestUtils.createTestRoutes(17);
        RecyclerView recyclerView = findViewById(R.id.route_recycler_view);
        TripAdapter tripAdapter = new TripAdapter(testRoutes);

        recyclerView.setAdapter(tripAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addRouteButton.setOnClickListener(view -> {
            int lastListIndex = testRoutes.size();
            testRoutes.addAll(TestUtils.createTestRoutes(5));
            tripAdapter.notifyItemInserted(lastListIndex);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            LoginRequest.remoteUserLogoutRequest(TripHomeActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
        });
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}