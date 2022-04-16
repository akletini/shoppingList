package com.akletini.shoppinglist.ui.item;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.request.RemoteItemRequest;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.math.BigDecimal;

public class ItemCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);
        initAmountPicker();
        initSubmitButton();
    }

    private void initSubmitButton() {
        MaterialButton button = findViewById(R.id.addItemButton);
        button.setOnClickListener(view -> {
            EditText itemPrice = findViewById(R.id.itemPrice);
            ItemDto itemDto = new ItemDto();
            itemDto.setName(ViewUtils.textViewToString(findViewById(R.id.itemName)));
            itemDto.setDescription(ViewUtils.textViewToString(findViewById(R.id.itemDescription)));
            itemDto.setAmount(Integer.parseInt(ViewUtils.textViewToString(findViewById(R.id.numberDisplay))));
            double itemPriceDouble = Double.parseDouble(itemPrice.getText().toString());
            itemDto.setPrice(BigDecimal.valueOf(itemPriceDouble));
            try {
                RemoteItemRequest.remoteItemCreateRequest(this, itemDto);
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
        });

        decrement.setOnClickListener(view -> {
            int currentValue = Integer.parseInt(numberDisplay.getText().toString());
            if (currentValue > 1) {
                numberDisplay.setText(String.valueOf(currentValue - 1));
                if (currentValue == 2) {
                    decrement.setEnabled(false);
                }
            }
        });
    }


}