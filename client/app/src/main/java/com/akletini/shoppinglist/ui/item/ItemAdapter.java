package com.akletini.shoppinglist.ui.item;

import static java.util.Comparator.comparing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.ItemDto;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<ItemDto> items;
    private final List<ItemDto> itemsCopy;
    private final OnItemClickListener onItemClickListener;
    private int selectedPos = RecyclerView.NO_POSITION;
    private List<Integer> selectedPositions;

    public ItemAdapter(List<ItemDto> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
        itemsCopy = new ArrayList<>();
        itemsCopy.addAll(items);
        selectedPositions = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_layout, parent, false);
        return new ItemAdapter.ViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        // this may be slow to put here
        items.sort(comparing(ItemDto::getName));
        ItemDto itemDto = items.get(position);
        View itemView = holder.itemView;

        holder.itemName.setText(itemDto.getName());
        itemView.setSelected(selectedPos == position);
        if (selectedPos == position) {
            if (!selectedPositions.contains(position)) {
                selectedPositions.add(position);
            } else {
                selectedPositions.remove(Integer.valueOf(position));
            }
        }
        if (!selectedPositions.contains(position)) {
            itemView.setBackgroundColor(Color.WHITE);
            itemView.setBackground(ResourcesCompat
                    .getDrawable(itemView.getResources(), R.drawable.customborder, null));
        } else {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

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
            for (ItemDto item : itemsCopy) {
                if (item.getName().toLowerCase().contains(text)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<ItemDto> getItems() {
        return items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        OnItemClickListener onItemClickListener;
        public List<Integer> selectedItems = new ArrayList<>();

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
            onItemClickListener.onItemClick(getAdapterPosition());
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
