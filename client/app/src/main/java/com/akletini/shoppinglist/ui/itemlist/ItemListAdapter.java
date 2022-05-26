package com.akletini.shoppinglist.ui.itemlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.ItemListDto;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private final List<ItemListDto> items;
    private final List<ItemListDto> itemsCopy;
    private final ItemListAdapter.OnItemClickListener onItemClickListener;

    public ItemListAdapter(List<ItemListDto> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
        itemsCopy = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_list_layout, parent, false);
        return new ItemListAdapter.ViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {

        holder.listName.setText(items.get(position).getName());
        holder.listMarket.setText(items.get(position).getMarket().getStore());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        items.clear();
        if (text.isEmpty()) {
            items.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (ItemListDto itemList : itemsCopy) {
                if (itemList.getName().toLowerCase().contains(text)) {
                    items.add(itemList);
                }
            }
        }
        notifyDataSetChanged();
    }


    public List<ItemListDto> getItems() {
        return items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView listName, listMarket;
        ItemListAdapter.OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, ItemListAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            listName = itemView.findViewById(R.id.itemListName);
            listMarket = itemView.findViewById(R.id.itemListMarket);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
