package com.popcornchicken.tindine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.util.zip.GZIPOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestInfoActivity extends Activity {
    @BindView(R.id.request_info_restaurant_name) TextView restaurantName;
    @BindView(R.id.request_info_restaurant_address) TextView restaurantAddress;
    @BindView(R.id.request_info_lunch_topic_1) TextView lunchTopic1;
    @BindView(R.id.request_info_lunch_topic_2) TextView lunchTopic2;
    @BindView(R.id.request_info_status) TextView status;
    @BindView(R.id.request_info_requester_profile_picture) ImageView requesterProfilePicture;
    @BindView(R.id.request_info_button2) Button button2;
    @BindView(R.id.request_info_button3) Button button3;

    String requestViewingState;
    String requesterId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_request_info);
        ButterKnife.bind(this);
        populateFields(getIntent());
    }

    private void populateFields(Intent intent) {
        final Request request = (Request) intent.getSerializableExtra("RequestObject");
        if (request != null) {
            requesterId = request.getRequesterID();
            status.setText(request.getRequestState());
            final RequestData requestData = request.getRequestData();
            if (requestData != null) {
                restaurantName.setText(requestData.getRestaurant().getRestaurantName());
                restaurantAddress.setText(requestData.getRestaurant().getRestaurantAddress());
                lunchTopic1.setText(requestData.getTopic1());
                lunchTopic2.setText(requestData.getTopic2());
            }

            final boolean isRequester = requesterId.equals(Profile.getCurrentProfile().getId());
            switch (request.getRequestState()) {
                case RequestState.PENDING:
                    requestViewingState = isRequester ? RequestViewingState.REQUESTER_PENDING : RequestViewingState.ACCEPTOR_PENDING;
                    break;
                case RequestState.CLAIMED:
                    requestViewingState = isRequester ? RequestViewingState.REQUESTER_CLAIMED : RequestViewingState.ACCEPTOR_CLAIMED;
                    break;
                case RequestState.COMPLETED:
                    requestViewingState = isRequester ? RequestViewingState.REQUESTER_COMPLETED : RequestViewingState.ACCEPTOR_COMPLETED;
                    break;
            }

            initializeButtons();
            final String profilePicUrl = "https://graph.facebook.com/" + requesterId + "/picture?type=square";
            Picasso.with(getApplicationContext()).load(profilePicUrl).into(requesterProfilePicture);
        }
    }

    private void initializeButtons() {
        switch (requestViewingState) {
            case RequestViewingState.REQUESTER_PENDING:
                button2.setText(getResources().getString(R.string.remove_request));
                button3.setVisibility(View.GONE);
                break;
            case RequestViewingState.REQUESTER_CLAIMED:
                button2.setText(getResources().getString(R.string.unmatch_request));
                break;
            case RequestViewingState.REQUESTER_COMPLETED:
                button2.setVisibility(View.GONE);
                button3.setVisibility(View.GONE);
                break;
            case RequestViewingState.ACCEPTOR_PENDING:
                button2.setText(getResources().getString(R.string.claim));
                button3.setVisibility(View.GONE);
                break;
            case RequestViewingState.ACCEPTOR_CLAIMED:
                button2.setText(getResources().getString(R.string.unclaim_request));
                button3.setVisibility(View.GONE);
                break;
            case RequestViewingState.ACCEPTOR_COMPLETED:
                button2.setVisibility(View.GONE);
                button3.setVisibility(View.GONE);
                break;
        }
    }

    // Always cancel button
    @OnClick(R.id.request_info_button1)
    public void button1Clicked() {
        finish();
    }

    @OnClick(R.id.request_info_button2)
    public void button2Clicked() {
        switch (requestViewingState) {
            case RequestViewingState.REQUESTER_PENDING:
                // remove request
                break;
            case RequestViewingState.REQUESTER_CLAIMED:
                // un-match request
                break;
            case RequestViewingState.ACCEPTOR_PENDING:
                // claim request
                break;
            case RequestViewingState.ACCEPTOR_CLAIMED:
                // un-claim request
                break;
        }
    }

    // Always complete button
    @OnClick(R.id.request_info_button3)
    public void button3Clicked() {
        // complete request
    }

    @OnClick(R.id.request_info_requester_profile_picture)
    public void requesterProfilePictureClicked() {
        String url = "http://www.facebook.com/" + requesterId;
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
