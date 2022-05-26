package com.akletini.shoppinglist.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.data.model.MarketDto;
import com.akletini.shoppinglist.data.model.RouteDto;
import com.akletini.shoppinglist.data.model.UserDto;
import com.akletini.shoppinglist.request.RemoteItemRequest;
import com.akletini.shoppinglist.ui.login.LoginActivity;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestUtils {

    private TestUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<RouteDto> createTestRoutes(int numSamples) {
        List<RouteDto> testRoutes = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            RouteDto routeDto = new RouteDto();
            routeDto.setName("Route_" + i);
            UserDto userDto = new UserDto();
            userDto.setUsername("akletini");
            routeDto.setOwner(userDto);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            String stringDate = sdf.format(date);
            routeDto.setCreationDate(stringDate);
            List<ItemListDto> itemLists = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ItemListDto itemListDto = new ItemListDto();
                MarketDto marketDto = new MarketDto();
                marketDto.setStore("Market_" + j);
                itemListDto.setMarket(marketDto);
                itemLists.add(itemListDto);
            }
            routeDto.setItemLists(itemLists);
            testRoutes.add(routeDto);
        }
        return testRoutes;
    }

    private static List<ItemDto> createTestItems(int numSamples) {
        List<ItemDto> testItems = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            ItemDto item = new ItemDto();
            item.setName("Item_" + i);
            item.setDescription("Description_" + i);
            item.setPrice(new BigDecimal(21 / (i + 1)));
            item.setAmount(50 % (i + 1));
            testItems.add(item);
        }
        return testItems;
    }

    public static void test_remoteItemCreateRequest(final Context context) throws JSONException {
        int nSamples = 15;
        List<ItemDto> testItems = createTestItems(nSamples);
        for (int i = 0; i < nSamples; i++) {
            RemoteItemRequest.remoteItemCreateRequest(context, LoginActivity.class, testItems.get(i));
        }
    }
}
