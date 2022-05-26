package com.akletini.shoppinglist.request;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.akletini.shoppinglist.data.datastore.DataStoreRepository;
import com.akletini.shoppinglist.data.datastore.MarketDataStore;
import com.akletini.shoppinglist.data.model.MarketDto;
import com.akletini.shoppinglist.ui.market.MarketHomeActivity;
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

public class RemoteMarketRequest {

    public static final String MARKET_PATH = "/market";

    public static void remoteMarketCreateRequest(Context context, MarketDto marketDto) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + MARKET_PATH + "/createMarket";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(marketDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final MarketDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), MarketDto.class);
                MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
                marketDataStore.addElement(responseObject);
                final Intent intent = new Intent(context, MarketHomeActivity.class);
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

    public static void remoteMarketModifyRequest(Context context, MarketDto marketDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + MARKET_PATH + "/updateMarket";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final String jsonObject = new Gson().toJson(marketDto);
        final JSONObject requestObject = new JSONObject(jsonObject);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestObject, response -> {
            final MarketDto responseObject;
            if (response != null) {
                final Gson gson = new Gson();
                responseObject = gson.fromJson(response.toString(), MarketDto.class);
                MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
                marketDataStore.modifyElement(responseObject);
                if (redirect) {
                    final Intent intent = new Intent(context, targetActivityClass);
                    intent.putExtra("caller", "MarketEditActivity");
                    assert responseObject != null;
                    intent.putExtra("market_id", responseObject.getId());
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

    public static void remoteMarketDeleteRequest(Context context, MarketDto marketDto, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + MARKET_PATH + "/deleteMarket/" + marketDto.getId().toString();

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
            marketDataStore.deleteElement(marketDto);
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

    public static void remoteMarketGetAllRequest(final Context context, Class<?> targetActivityClass, boolean redirect) throws JSONException {
        final String url = SingletonRequestQueue.BASE_URL + MARKET_PATH + "/getMarkets";

        final RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response != null) {
                final List<JSONObject> responseList = HTTPUtils.jsonArrayToJsonObject(response);
                final Gson gson = new Gson();
                for (JSONObject jsonObject : responseList) {
                    MarketDto responseObject = gson.fromJson(jsonObject.toString(), MarketDto.class);
                    MarketDataStore marketDataStore = (MarketDataStore) DataStoreRepository.getDataStore(MarketDto.class);
                    marketDataStore.addElement(responseObject);
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
