package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.ItemListDataStore;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.ui.itemlist.ItemListHomeActivity;
import com.akletini.shoppinglist.utils.HTTPUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RemoteItemListRequest {

    public static final String ITEMLIST_PATH = "/itemList";

    public static void remoteItemListCreateRequest(final Context context, final ItemListDto itemListDto) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + ITEMLIST_PATH + "/createItemList";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(itemListDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final ItemListDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), ItemListDto.class);
                responseObject.setItemAmountMap(createItemAmountMap(responseObject));
                ItemListDataStore itemListDataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
                itemListDataStore.addElement(responseObject);
                final Intent intent = new Intent(context, ItemListHomeActivity.class);
                context.startActivity(intent);
            }
        }, error -> Toast.makeText(context,
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteItemListGetAllRequest(final Context context, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + ITEMLIST_PATH + "/getItemLists";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();
                for (JSONObject jsonObject : responseList) {
                    ItemListDto responseObject = gson.fromJson(jsonObject.toString(), ItemListDto.class);
                    ItemListDataStore dataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
                    dataStore.addElement(responseObject);
                }
                if (redirect) {
                    final Intent intent = new Intent(context, targetActivityClass);
                    context.startActivity(intent);
                }
            }
        }, error -> Toast.makeText(context,
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteMarketModifyRequest(Context context, ItemListDto itemListDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + ITEMLIST_PATH + "/updateItemList";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(itemListDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final ItemListDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), ItemListDto.class);
                ItemListDataStore dataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
                dataStore.modifyElement(responseObject);
                if (redirect) {
                    final Intent intent = new Intent(context, targetActivityClass);
                    intent.putExtra("caller", "ItemListCreateDetailsActivity");
                    assert responseObject != null;
                    intent.putExtra("itemList_id", responseObject.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                }
            }
        }, error -> Toast.makeText(context,
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void remoteMarketDeleteRequest(Context context, ItemListDto itemListDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + ITEMLIST_PATH + "/deleteItemList/" + itemListDto.getId().toString();

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            ItemListDataStore dataStore = (ItemListDataStore) DataStoreRepository.getDataStore(ItemListDto.class);
            dataStore.deleteElement(itemListDto);
            if (redirect) {
                final Intent intent = new Intent(context, targetActivityClass);
                context.startActivity(intent);
            }

        }, error -> Toast.makeText(context,
                HTTPUtils.buildErrorFromHTTPResponse(error),
                Toast.LENGTH_SHORT).show());

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private static Map<ItemDto, Integer> createItemAmountMap(ItemListDto itemList) {
        Map<ItemDto, Integer> map = new LinkedHashMap<>();
        List<ItemDto> items = itemList.getItemList();
        List<Integer> itemAmounts = itemList.getItemAmounts();

        for (int i = 0; i < Math.min(items.size(), itemAmounts.size()); i++) {
            map.put(items.get(i), itemAmounts.get(i));
        }
        return map;
    }
}
