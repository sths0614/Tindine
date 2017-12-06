package com.popcornchicken.tindine;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<Request> {
    private ArrayList<Request> mRequests;
    private Context mContext;
    ViewHolder holder;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        mRequests = requests;
        mContext = context;
    }

    private class ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantAddress;
        TextView requestStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the request item for this position
        final Request request = getItem(position);
        holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
            holder = new ViewHolder();

            holder.restaurantImage = (ImageView) convertView.findViewById(R.id.item_request_restaurant_image);
            holder.restaurantName = (TextView) convertView.findViewById(R.id.item_request_restaurant_name);
            holder.restaurantAddress = (TextView) convertView.findViewById(R.id.item_request_restaurant_address);
            holder.requestStatus = (TextView) convertView.findViewById(R.id.item_request_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                Picasso.with(getContext()).load(stringBuilder.toString()).into(holder.restaurantImage);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpClient httpClient = new DefaultHttpClient();
                final String placesUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + request.getRequestData().getRestaurant().getRestaurantID() + "&key=" + getContext().getResources().getString(R.string.google_api);
                final HttpGet httpGet = new HttpGet(placesUrl);
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String serverResponse = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("photos");
                    String photoReference = jsonArray.getJSONObject(0).getString("photo_reference");

                    String photoReferenceUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + getContext().getResources().getString(R.string.google_api);;
                    stringBuilder.append(photoReferenceUrl);
                    handler.postDelayed(runnable, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        holder.restaurantName.setText(request.getRequestData().getRestaurant().getRestaurantName());
        holder.restaurantAddress.setText(request.getRequestData().getRestaurant().getRestaurantAddress());
        holder.requestStatus.setText(request.getRequestState());

        return convertView;
    }
}
