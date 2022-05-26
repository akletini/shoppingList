package com.akletini.shoppinglist.ui.item;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.ItemDataStore;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.request.RemoteItemRequest;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.math.BigDecimal;

public class ItemCreateActivity extends AppCompatActivity {

    EditText itemName, itemDescription, itemAmount, itemPrice;
    MaterialButton submitButton, deleteButton;
    boolean isExistingItem = false;
    long currentExistingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemAmount = findViewById(R.id.numberDisplay);
        itemPrice = findViewById(R.id.itemPrice);
        String callerClass = getIntent().getStringExtra("caller");
        if (callerClass != null && (callerClass.equals("ItemHomeActivity") || callerClass.equals("ItemCreateActivity"))) {
            initViewWithExistingItem(getIntent().getLongExtra("item_id", 0));
            isExistingItem = true;
        } else {
            isExistingItem = false;
        }
        initAmountPicker();
        initSubmitButton();
    }

    private void initViewWithExistingItem(long id) {
        ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getDataStore(ItemDto.class);
        ItemDto item = itemDataStore.getElementById(id);
        if (item != null) {
            itemName.setText(item.getName());
            itemDescription.setText(item.getDescription());
            itemAmount.setText(item.getAmount().toString());
            itemPrice.setText(item.getPrice().toString());
            currentExistingId = item.getId();
        }
    }

    private void initSubmitButton() {
        submitButton = findViewById(R.id.addItemButton);
        submitButton.setText(isExistingItem ? "Update" : "Add item");
        submitButton.setOnClickListener(this::onClickSubmit);

        deleteButton = findViewById(R.id.remoteItemButton);
        deleteButton.setEnabled(isExistingItem);
        deleteButton.setVisibility(isExistingItem ? View.VISIBLE : View.INVISIBLE);
        deleteButton.setOnClickListener(view -> {
            ItemDto itemDto = new ItemDto();
            itemDto.setId(isExistingItem ? currentExistingId : null);
            itemDto.setName(ViewUtils.textViewToString(itemName));
            itemDto.setDescription(ViewUtils.textViewToString(itemDescription));
            itemDto.setAmount(Integer.parseInt(ViewUtils.textViewToString(itemAmount)));
            double itemPriceDouble = Double.parseDouble(ViewUtils.textViewToString(itemPrice));
            itemDto.setPrice(BigDecimal.valueOf(itemPriceDouble));
            try {
                RemoteItemRequest.remoteItemDeleteRequest(this, itemDto, ItemHomeActivity.class, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void initAmountPicker() {
        Button increment = findViewById(R.id.increment);
        Button decrement = findViewById(R.id.decrement);
        EditText numberDisplay = findViewById(R.id.numberDisplay);
        int initialValue = Integer.parseInt(numberDisplay.getText().toString());
        if (initialValue == 1) {
            decrement.setEnabled(false);
        }

        numberDisplay.setOnFocusChangeListener((view, b) -> {
            numberDisplay.setSelection(numberDisplay.getText().length());
        });

        increment.setOnClickListener(view -> {
            decrement.setEnabled(true);
            int currentValue = Integer.parseInt(numberDisplay.getText().toString());
            numberDisplay.setText(String.valueOf(currentValue + 1));
            decrement.setBackgroundColor(Color.parseColor("#c71c10"));
        });

        decrement.setOnClickListener(view -> {
            int currentValue = Integer.parseInt(numberDisplay.getText().toString());
            if (currentValue > 1) {
                numberDisplay.setText(String.valueOf(currentValue - 1));
                if (currentValue == 2) {
                    decrement.setEnabled(false);
                    decrement.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ItemHomeActivity.class));
    }

    private void onClickSubmit(View view) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(isExistingItem ? currentExistingId : null);
        String itemNameAsString = ViewUtils.textViewToString(itemName);
        if (itemNameAsString.isEmpty()) {
            Toast.makeText(this, "Username may not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else {
            itemDto.setName(itemNameAsString);
        }
        itemDto.setDescription(ViewUtils.textViewToString(itemDescription));
        itemDto.setAmount(Integer.parseInt(ViewUtils.textViewToString(itemAmount)));
        final boolean isNumberEmpty = ViewUtils.textViewToString(itemPrice).isEmpty();
        double itemPriceDouble = Double.parseDouble(isNumberEmpty ? "0" : ViewUtils.textViewToString(itemPrice));
        itemDto.setPrice(BigDecimal.valueOf(itemPriceDouble));
        try {
            if (isExistingItem) {
                RemoteItemRequest.remoteItemModifyRequest(this, itemDto, ItemCreateActivity.class, true);
            } else {
                RemoteItemRequest.remoteItemCreateRequest(this, ItemHomeActivity.class, itemDto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}