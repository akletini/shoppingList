package com.akletini.shoppinglist.ui.route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akletini.shoppinglist.R;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.data.model.RouteDto;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final List<RouteDto> routeList;

    public RouteAdapter(List<RouteDto> routeList) {
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View routeListView = inflater.inflate(R.layout.route_layout, parent, false);

        return new ViewHolder(routeListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteDto routeDto = routeList.get(position);

        holder.routeName.setText(routeDto.getName());
        holder.markets.setText(concatMarkets(getMarketsFromItemLists(routeDto.getItemLists())));
        holder.creationDate.setText(routeDto.getCreationDate().toString());
        holder.owner.setText(routeDto.getOwner().getUsername());

    }

    private List<String> getMarketsFromItemLists(List<ItemListDto> itemLists) {
        List<String> marketNames = new ArrayList<>();
        for (ItemListDto itemList : itemLists) {
            marketNames.add(itemList.getMarket().getStore());
        }
        return marketNames;
    }

    private StringBuilder concatMarkets(List<String> markets) {
        StringBuilder returnString = new StringBuilder();
        for (String market : markets) {
            returnString.append(market).append(", ");
        }
        return returnString;
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView routeName, markets, owner, creationDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            routeName = itemView.findViewById(R.id.routeEntry);
            markets = itemView.findViewById(R.id.route_markets);
            owner = itemView.findViewById(R.id.route_owner);
            creationDate = itemView.findViewById(R.id.route_creation_date);
        }
    }
}
