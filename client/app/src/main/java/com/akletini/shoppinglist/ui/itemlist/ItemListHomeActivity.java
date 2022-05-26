package com.akletini.shoppinglist.ui.itemlist;

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
import com.akletini.shoppinglist.data.datastore.ItemListDataStore;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.request.RemoteItemListRequest;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.request.SingletonRequestQueue;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
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

public class ItemListHomeActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    ItemListAdapter itemListAdapter;
    SwipeRefreshLayout refreshLayout;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_home);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.menuItemItemlist).setEnabled(false);
        FloatingActionButton addRouteButton = findViewById(R.id.addItemListButton);
        addRouteButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ItemListCreateActivity.class));
        });
        setDrawerMenuUsername();
        setLogoutListener();
        ItemListDataStore dataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
        RecyclerView recyclerView = findViewById(R.id.itemList_recycler_view);
        final List<ItemListDto> copyList = new ArrayList<>();
        for (ItemListDto itemList : dataStore.getAllElements()) {
            copyList.add(itemList.copy());
        }

        itemListAdapter = new ItemListAdapter(copyList, this);

        recyclerView.setAdapter(itemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout = findViewById(R.id.itemListRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
        initSearchView(itemListAdapter);

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
            RemoteUserRequest.remoteUserLogoutRequest(ItemListHomeActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
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
    }

    public void switchToMarketEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, MarketHomeActivity.class));
    }

    public void switchToRouteEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, RouteHomeActivity.class));
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        final String url = SingletonRequestQueue.BASE_URL + RemoteItemListRequest.ITEMLIST_PATH + "/getItemLists";

        final RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();

                final ItemListDataStore dataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
                dataStore.getAllElements().clear();
                for (JSONObject jsonObject : responseList) {
                    ItemListDto responseObject = gson.fromJson(jsonObject.toString(), ItemListDto.class);
                    dataStore.addElement(responseObject);
                }
                List<ItemListDto> items = itemListAdapter.getItems();
                items.clear();
                items.addAll(dataStore.getAllElements());
                itemListAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position) {
        ItemListDto clickedItemList = itemListAdapter.getItems().get(position);
        Intent intent = new Intent(this, ItemListCreateDetailsActivity.class);
        intent.putExtra("item_id", clickedItemList.getId());
        intent.putExtra("caller", "ItemListHomeActivity");
        startActivity(intent);
    }

    private void initSearchView(ItemListAdapter itemAdapter) {
        searchView = findViewById(R.id.itemSearchView);
        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            TextView toolbarTextView = findViewById(R.id.tv_toolbar_custom);
            if (!toolbarTextView.getText().equals("")) {
                toolbarTextView.setText("");
            } else {
                toolbarTextView.setText("Shopping Lists");
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
}