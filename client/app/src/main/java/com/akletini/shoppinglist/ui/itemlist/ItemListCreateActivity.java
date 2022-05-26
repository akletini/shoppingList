package com.akletini.shoppinglist.ui.itemlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import com.akletini.shoppinglist.ui.item.ItemAdapter;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
import com.akletini.shoppinglist.ui.market.MarketHomeActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ItemListCreateActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    SearchView searchView;
    ItemAdapter itemAdapter;
    SwipeRefreshLayout refreshLayout;
    TextView toolbarTextView;
    private ArrayList<ItemDto> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_home);

        selectedItems = new ArrayList<>();

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
            Intent intent = new Intent(this, ItemListCreateDetailsActivity.class);
            intent.putExtra("selection", selectedItems);
            intent.putExtra("caller", "ItemListCreateActivity");
            startActivity(intent);
        });
        ItemDataStore dataStore = (ItemDataStore) DataStoreRepository.getDataStore(ItemDto.class);
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

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void initSearchView(ItemAdapter itemAdapter) {
        searchView = findViewById(R.id.itemSearchView);
        toolbarTextView = findViewById(R.id.tv_toolbar_custom);
        toolbarTextView.setText("Add Articles");
        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            if (!toolbarTextView.getText().equals("")) {
                toolbarTextView.setText("");
            } else {
                toolbarTextView.setText("Add Articles");
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
            RemoteUserRequest.remoteUserLogoutRequest(ItemListCreateActivity.this, LoggedInUserSingleton.getInstance().getCurrentUser());
        });
    }

    public void switchToArticleEditor(final MenuItem menuItem) {
        startActivity(new Intent(this, ItemHomeActivity.class));
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
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onItemClick(int position) {
        ItemDto clickedItem = itemAdapter.getItems().get(position);
        if (!selectedItems.contains(clickedItem)) {
            selectedItems.add(clickedItem);
        } else {
            selectedItems.remove(clickedItem);
        }
    }
}
