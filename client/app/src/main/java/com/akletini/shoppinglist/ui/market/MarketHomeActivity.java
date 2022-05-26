package com.akletini.shoppinglist.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.datastore.MarketDataStore;
import com.akletini.shoppinglist.data.model.MarketDto;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.request.SingletonRequestQueue;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
import com.akletini.shoppinglist.ui.itemlist.ItemListHomeActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;
import com.akletini.shoppinglist.utils.HTTPUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarketHomeActivity extends AppCompatActivity implements MarketAdapter.OnMarketClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    SearchView searchView;
    MarketAdapter marketAdapter;
    SwipeRefreshLayout refreshLayout;

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
        initAddButton();
        initSearchView(marketAdapter);

        MarketDataStore dataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
        RecyclerView recyclerView = findViewById(R.id.market_recycler_view);

        final List<MarketDto> copyList = new ArrayList<>();
        for (MarketDto market : dataStore.getAllElements()) {
            copyList.add(market.copy());
        }
        marketAdapter = new MarketAdapter(copyList, this);

        recyclerView.setAdapter(marketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout = findViewById(R.id.marketRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initSearchView(MarketAdapter marketAdapter) {
        searchView = findViewById(R.id.marketSearchView);
        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            TextView toolbarTextView = findViewById(R.id.tv_toolbar_custom);
            if (!toolbarTextView.getText().equals("")) {
                toolbarTextView.setText("");
            } else {
                toolbarTextView.setText("Markets");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                marketAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                marketAdapter.filter(newText);
                return true;
            }
        });
    }

    private void initAddButton() {
        FloatingActionButton addButton = findViewById(R.id.addMarketButton);
        addButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MarketEditActivity.class));
        });
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

    @Override
    public void onMarketClick(int position) {
        MarketDto clickedMarket = marketAdapter.getMarkets().get(position);
        Intent intent = new Intent(this, MarketEditActivity.class);
        intent.putExtra("market_id", clickedMarket.getId());
        intent.putExtra("caller", "MarketHomeActivity");
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        final String url = SingletonRequestQueue.BASE_URL + "/market/getMarkets";

        final RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();

                final MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
                marketDataStore.getAllElements().clear();
                for (JSONObject jsonObject : responseList) {
                    MarketDto responseObject = gson.fromJson(jsonObject.toString(), MarketDto.class);
                    marketDataStore.addElement(responseObject);
                }
                List<MarketDto> markets = marketAdapter.getMarkets();
                markets.clear();
                markets.addAll(marketDataStore.getAllElements());
                marketAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, error -> Toast.makeText(this,
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}