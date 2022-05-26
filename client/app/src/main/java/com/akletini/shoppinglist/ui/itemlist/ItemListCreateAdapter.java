package com.akletini.shoppinglist.ui.itemlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ItemListCreateAdapter extends RecyclerView.Adapter<ItemListCreateAdapter.ViewHolder> {

    private final List<ItemDto> items;
    private final List<ItemDto> itemsCopy;
    private final ItemListCreateAdapter.OnItemClickListener onItemClickListener;

    public ItemListCreateAdapter(List<ItemDto> items, ItemListCreateAdapter.OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
        itemsCopy = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ItemListCreateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_amount_layout, parent, false);
        return new ItemListCreateAdapter.ViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListCreateAdapter.ViewHolder holder, int position) {

        holder.itemName.setText(items.get(position).getName());
        holder.itemAmount.setText(items.get(position).getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public List<ItemDto> getItems() {
        return items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public EditText itemAmount;
        ItemListCreateAdapter.OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, ItemListCreateAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.numberDisplay);
            MaterialButton decrement = itemView.findViewById(R.id.decrement);
            MaterialButton increment = itemView.findViewById(R.id.increment);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);

            itemAmount.setOnFocusChangeListener((view, b) -> {
                itemAmount.setSelection(itemAmount.getText().length());
            });

            increment.setOnClickListener(view -> {
                decrement.setEnabled(true);
                int currentValue = Integer.parseInt(itemAmount.getText().toString());
                itemAmount.setText(String.valueOf(currentValue + 1));
                decrement.setBackgroundColor(Color.parseColor("#c71c10"));
            });

            decrement.setOnClickListener(view -> {
                int currentValue = Integer.parseInt(itemAmount.getText().toString());
                if (currentValue > 1) {
                    itemAmount.setText(String.valueOf(currentValue - 1));
                    if (currentValue == 2) {
                        decrement.setEnabled(false);
                        decrement.setBackgroundColor(Color.LTGRAY);
                    }
                }
            });
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
