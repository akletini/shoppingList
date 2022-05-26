package com.akletini.shoppinglist.ui.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.MarketDto;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    private List<MarketDto> markets;
    private List<MarketDto> marketsCopy;
    private OnMarketClickListener onMarketClickListener;

    public MarketAdapter(List<MarketDto> markets, OnMarketClickListener onMarketClickListener) {
        this.markets = markets;
        this.onMarketClickListener = onMarketClickListener;
        marketsCopy = new ArrayList<>();
        marketsCopy.addAll(markets);
    }

    @NonNull
    @Override
    public MarketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View marketView = inflater.inflate(R.layout.market_layout, parent, false);
        return new MarketAdapter.ViewHolder(marketView, onMarketClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.ViewHolder holder, int position) {
        MarketDto marketDto = markets.get(position);
        holder.marketName.setText(marketDto.getStore());
        holder.marketLocation.setText(marketDto.getLocation());
    }

    @Override
    public int getItemCount() {
        return markets.size();
    }

    public List<MarketDto> getMarkets() {
        return markets;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        markets.clear();
        if (text.isEmpty()) {
            markets.addAll(marketsCopy);
        } else {
            text = text.toLowerCase();
            for (MarketDto market : marketsCopy) {
                if (market.getStore().toLowerCase().contains(text)) {
                    markets.add(market);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnMarketClickListener onMarketClickListener;
        public TextView marketName, marketLocation;

        public ViewHolder(@NonNull View itemView, OnMarketClickListener onMarketClickListener) {
            super(itemView);
            this.onMarketClickListener = onMarketClickListener;
            itemView.setOnClickListener(this);
            marketName = itemView.findViewById(R.id.marketName);
            marketLocation = itemView.findViewById(R.id.marketLocation);
        }

        @Override
        public void onClick(View view) {
            onMarketClickListener.onMarketClick(getAdapterPosition());
        }
    }

    public interface OnMarketClickListener {
        void onMarketClick(int position);
    }
}
