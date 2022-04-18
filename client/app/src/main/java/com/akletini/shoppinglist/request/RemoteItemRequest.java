package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.ItemDataStore;
import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.ui.item.ItemHomeActivity;
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

import java.util.List;

public class RemoteItemRequest {

    public static void remoteItemCreateRequest(final Context context, final ItemDto itemDto) throws JSONException {

        final String url = SingletonRequestQueue.BASE_URL + "/item/createItem";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(itemDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final ItemDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), ItemDto.class);
                ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
                itemDataStore.addElement(responseObject);
                final Intent intent = new Intent(context, ItemHomeActivity.class);
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

    public static void remoteItemModifyRequest(Context context, ItemDto itemDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + "/item/updateItem";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(itemDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final ItemDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), ItemDto.class);
                ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
                itemDataStore.modifyElement(responseObject);
                if (redirect) {
                    final Intent intent = new Intent(context, targetActivityClass);
                    intent.putExtra("caller", "ItemCreateActivity");
                    assert responseObject != null;
                    intent.putExtra("item_id", responseObject.getId());
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

    public static void remoteItemDeleteRequest(Context context, ItemDto itemDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + "/item/deleteItem/" + itemDto.getId().toString();

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
            itemDataStore.deleteElement(itemDto);
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

    public static void remoteItemGetAllRequest(final Context context, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + "/item/getItems";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();
                for (JSONObject jsonObject : responseList) {
                    ItemDto responseObject = gson.fromJson(jsonObject.toString(), ItemDto.class);
                    ItemDataStore itemDataStore = (ItemDataStore) DataStoreRepository.getInstance().getDataStore(ItemDto.class);
                    itemDataStore.addElement(responseObject);
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


}
