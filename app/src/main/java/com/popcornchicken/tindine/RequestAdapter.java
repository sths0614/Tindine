package com.popcornchicken.tindine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<Request> {
    private ArrayList<Request> mRequests;
    private Context mContext;

    public RequestAdapter(Context context, ArrayList<Request> requests){
        super(context, 0, requests);
        mRequests = requests;
        mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the request item for this position
        Request request = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent);
        }

        // Lookup view for data population
        ImageView restaurantImage = (ImageView) convertView.findViewById(R.id.item_request_restaurant_image);
        TextView restaurantName = (TextView) convertView.findViewById(R.id.item_request_restaurant_name);
        TextView restaurantAddress = (TextView) convertView.findViewById(R.id.item_request_restaurant_address);
        TextView requestStatus = (TextView) convertView.findViewById(R.id.item_request_status);

        restaurantName.setText(request.getRequestData().getRestaurant().getRestaurantName());
        restaurantAddress.setText(request.getRequestData().getRestaurant().getRestaurantAddress());
        requestStatus.setText(request.getRequestState());

        return convertView;
    }
}
