package com.akletini.shoppinglist.ui.market;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.MarketDataStore;
import com.akletini.shoppinglist.data.model.MarketDto;
import com.akletini.shoppinglist.request.RemoteMarketRequest;
import com.akletini.shoppinglist.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

public class MarketEditActivity extends AppCompatActivity {

    EditText marketName, marketLocation;
    MaterialButton submitButton, deleteButton;
    boolean isExistingMarket = false;
    long currentExistingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_edit);
        marketName = findViewById(R.id.marketName);
        marketLocation = findViewById(R.id.marketLocation);
        String callerClass = getIntent().getStringExtra("caller");
        if (callerClass != null && (callerClass.equals("MarketHomeActivity") || callerClass.equals("MarketEditActivity"))) {
            initViewWithExistingMarket(getIntent().getLongExtra("market_id", 0));
            isExistingMarket = true;
        } else {
            isExistingMarket = false;
        }
        initSubmitButton();
    }

    private void initViewWithExistingMarket(long id) {
        MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
        MarketDto market = marketDataStore.getElementById(id);
        if (market != null) {
            marketName.setText(market.getStore());
            marketLocation.setText(market.getLocation());
            currentExistingId = market.getId();
        }
    }

    private void initSubmitButton() {
        submitButton = findViewById(R.id.addMarketButton);
        submitButton.setText(isExistingMarket ? "Update" : "Add market");
        submitButton.setOnClickListener(this::onClickSubmit);

        deleteButton = findViewById(R.id.remoteMarketButton);
        deleteButton.setEnabled(isExistingMarket);
        deleteButton.setVisibility(isExistingMarket ? View.VISIBLE : View.INVISIBLE);
        deleteButton.setOnClickListener(view -> {
            MarketDto marketDto = new MarketDto();
            marketDto.setId(isExistingMarket ? currentExistingId : null);
            marketDto.setStore(ViewUtils.textViewToString(marketName));
            marketDto.setLocation(ViewUtils.textViewToString(marketLocation));
            try {
                RemoteMarketRequest.remoteMarketDeleteRequest(this, marketDto, MarketHomeActivity.class, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void onClickSubmit(View view) {
        MarketDto marketDto = new MarketDto();
        marketDto.setId(isExistingMarket ? currentExistingId : null);
        String marketNameAsString = ViewUtils.textViewToString(marketName);
        if (marketNameAsString.isEmpty()) {
            Toast.makeText(this, "Market name may not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else {
            marketDto.setStore(marketNameAsString);
        }
        marketDto.setLocation(ViewUtils.textViewToString(marketLocation));
        try {
            if (isExistingMarket) {
                RemoteMarketRequest.remoteMarketModifyRequest(this, marketDto, MarketEditActivity.class, true);
            } else {
                RemoteMarketRequest.remoteMarketCreateRequest(this, marketDto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}