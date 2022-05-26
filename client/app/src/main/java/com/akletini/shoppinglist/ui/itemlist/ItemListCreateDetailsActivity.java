package com.akletini.shoppinglist.ui.itemlist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.LoggedInUserSingleton;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.data.model.ItemListEntryDto;
import com.akletini.shoppinglist.data.model.MarketDto;
import com.akletini.shoppinglist.request.RemoteItemListRequest;
import com.akletini.shoppinglist.request.RemoteUserRequest;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
import com.akletini.shoppinglist.ui.market.MarketHomeActivity;
import com.akletini.shoppinglist.ui.route.RouteHomeActivity;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemListCreateDetailsActivity extends AppCompatActivity implements ItemListCreateAdapter.OnItemClickListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    Spinner marketSpinner;
    ItemListCreateAdapter itemAdapter;
    Button submitButton;
    RecyclerView recyclerView;
    ArrayList<ItemDto> selectedItems;
    TextView itemListName;

    boolean isExisting = false;
    ItemListDto currentItemList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_create_details);

        selectedItems = new ArrayList<>();
        itemListName = findViewById(R.id.itemListName);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        submitButton = findViewById(R.id.itemListSubmit);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.menuItemItemlist).setEnabled(false);
        setDrawerMenuUsername();
        setLogoutListener();
        initMarkets();

        Bundle extras = getIntent().getExtras();
        boolean isSelectionUpdate = extras.get("item_id") != null && extras.getBoolean("update");
        String callerClass = getIntent().getStringExtra("caller");
        if (callerClass != null && (callerClass.equals("ItemListCreateActivity"))
                && !isSelectionUpdate) {
            selectedItems = (ArrayList<ItemDto>) extras.get("selection");
        } else if ((callerClass != null && (callerClass.equals("ItemListHomeActivity")))
                || isSelectionUpdate) {
            Long itemListId = extras.getLong("item_id");
            currentItemList = (ItemListDto) DataStoreRepository.
                    getDataStore(ItemListDto.class).getElementById(itemListId);
            currentItemList.updateAmounts(currentItemList.getEntries());
            selectedItems = (ArrayList<ItemDto>) currentItemList.getItems();
            if (isSelectionUpdate) {
                selectedItems = (ArrayList<ItemDto>) extras.get("selection");
                List<ItemListEntryDto> entries = currentItemList.updateEntries(currentItemList.getEntries(), selectedItems, getNewItemAmounts(selectedItems));
                currentItemList.setEntries(entries);
            }
            ArrayAdapter<MarketDto> adapter = (ArrayAdapter<MarketDto>) marketSpinner.getAdapter();
            int position = adapter.getPosition(currentItemList.getMarket());
            marketSpinner.setSelection(position);
            itemListName.setText(currentItemList.getName());
            isExisting = true;
        }

        FloatingActionButton actionButton = findViewById(R.id.addItemsButton);
        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ItemListCreateActivity.class);
            intent.putExtra("caller", "ItemListCreateDetailsActivity");
            intent.putExtra("currentItemList", currentItemList.getId());
            intent.putExtra("selection", selectedItems);
            startActivity(intent);
        });

        final List<ItemDto> finalSelectedItems = selectedItems;
        submitButton.setOnClickListener(view -> {
            if (!isExisting) {
                setUpCreateRequest(finalSelectedItems);
            } else {
                setUpUpdateRequest(finalSelectedItems);
            }
        });

        recyclerView = findViewById(R.id.item_list_recycler_view);
        final List<ItemDto> copyList = new ArrayList<>();
        for (ItemDto item : selectedItems) {
            copyList.add(item.copy());
        }

        itemAdapter = new ItemListCreateAdapter(copyList, this);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpUpdateRequest(List<ItemDto> finalSelectedItems) {
        ItemListDto itemList = currentItemList.copy();
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final ItemListCreateAdapter.ViewHolder holder = (ItemListCreateAdapter.ViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            selectedItems.get(i).setAmount(Integer.parseInt(holder.itemAmount.getText().toString()));
        }
        itemList.setEntries(itemList.updateEntries(itemList.getEntries(), selectedItems, getNewItemAmounts(selectedItems)));
        itemList.setMarket((MarketDto) marketSpinner.getSelectedItem());
        itemList.setName(ViewUtils.textViewToString(itemListName));
        try {
            RemoteItemListRequest.remoteMarketModifyRequest(this, itemList, ItemListHomeActivity.class, true);
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to save Item list", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMarkets() {
        marketSpinner = findViewById(R.id.itemListMarket);
        MarketDto[] availableMarkets = DataStoreRepository
                .getDataStore(MarketDto.class).getAllElements().toArray(new MarketDto[0]);
        ArrayAdapter<MarketDto> arrayAdapter = new ArrayAdapter<MarketDto>(this, android.R.layout.simple_spinner_item, availableMarkets);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marketSpinner.setAdapter(arrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpCreateRequest(List<ItemDto> selectedItems) {
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setOwner(LoggedInUserSingleton.getInstance().getCurrentUser());
        itemListDto.setCreationDate(LocalDateTime.now().toString());

        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final ItemListCreateAdapter.ViewHolder holder = (ItemListCreateAdapter.ViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            selectedItems.get(i).setAmount(Integer.parseInt(holder.itemAmount.getText().toString()));
        }
        itemListDto.setEntries(itemListDto.createEntries(selectedItems, getNewItemAmounts(selectedItems)));
        itemListDto.setMarket((MarketDto) marketSpinner.getSelectedItem());
        itemListDto.setName(ViewUtils.textViewToString(itemListName));
        try {
            RemoteItemListRequest.remoteItemListCreateRequest(this, itemListDto);
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to save Item list", Toast.LENGTH_SHORT).show();
        }
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
            RemoteUserRequest.remoteUserLogoutRequest(this, LoggedInUserSingleton.getInstance().getCurrentUser());
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
    public void onItemClick(int position) {

    }

    private List<Integer> getNewItemAmounts(List<ItemDto> items) {
        List<Integer> amounts = new ArrayList<>();
        for (ItemDto item : items) {
            amounts.add(item.getAmount());
        }
        return amounts;
    }
}