package com.akletini.shoppinglist.ui.item;

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
import com.akletini.shoppinglist.data.datastore.ItemDataStore;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.request.SingletonRequestQueue;
import com.akletini.shoppinglist.ui.itemlist.ItemListHomeActivity;
import com.akletini.shoppinglist.ui.market.MarketHomeActivity;
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

public class ItemHomeActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    SearchView searchView;
    ItemAdapter itemAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_home);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.menuItemArticles).setEnabled(false);
        setDrawerMenuUsername();
        setLogoutListener();
        FloatingActionButton addRouteButton = findViewById(R.id.addItemButton);
        addRouteButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ItemCreateActivity.class));
        });
        ItemDataStore dataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
        RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        final List<ItemDto> copyList = new ArrayList<>();
        for (ItemDto item : dataStore.getAllElements()) {
            copyList.add(item.copy());
        }
        
        itemAdapter = new ItemAdapter(copyList, this);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout = findViewById(R.id.itemRefreshLayout);
        refreshLayout.setOnRefreshListener(this);

        initSearchView(itemAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    private void setDrawerMenuUsername() {
        NavigationView navView = findViewById(R.id.navigation_view);
        Menu menu = navView.getMenu();
        MenuItem item = menu.findItem(R.id.menuUsername);
        final String username = LoggedInUserSingleton.getInstance().getCurrentUser().getUsername();
        item.setTitle(username);
    }

    private void setLogoutListener() {
        final TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            RemoteUserRequest.remoteUserLogoutRequest(ItemHomeActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
        });
    }

    private void initSearchView(ItemAdapter itemAdapter) {
        searchView = findViewById(R.id.itemSearchView);
        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            TextView toolbarTextView = findViewById(R.id.tv_toolbar_custom);
            if (!toolbarTextView.getText().equals("")) {
                toolbarTextView.setText("");
            } else {
                toolbarTextView.setText("Articles");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        searchView.setQuery("", false);
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    public void switchToArticleEditor(final MenuItem menuItem) {
    }

    public void switchToItemListEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, ItemListHomeActivity.class));
    }

    public void switchToMarketEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, MarketHomeActivity.class));
    }

    public void switchToRouteEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, RouteHomeActivity.class));
    }

    @Override
    public void onItemClick(int position) {
        ItemDto clickedItem = itemAdapter.getItems().get(position);
        Intent intent = new Intent(this, ItemCreateActivity.class);
        intent.putExtra("item_id", clickedItem.getId());
        intent.putExtra("caller", "ItemHomeActivity");
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        final String url = SingletonRequestQueue.BASE_URL + "/item/getItems";

        final RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();

                final ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
                itemDataStore.getAllElements().clear();
                for (JSONObject jsonObject : responseList) {
                    ItemDto responseObject = gson.fromJson(jsonObject.toString(), ItemDto.class);
                    itemDataStore.addElement(responseObject);
                }
                List<ItemDto> items = itemAdapter.getItems();
                items.clear();
                items.addAll(itemDataStore.getAllElements());
                itemAdapter.notifyDataSetChanged();
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