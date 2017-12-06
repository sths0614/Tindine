package com.popcornchicken.tindine;

import android.content.Context;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class FirebaseManager {

    private static final String TAG = "FirebaseManager";  // for debugging

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRequestDatabase;

    private OnDataReadyListener mListener;

    public FirebaseManager(Context context) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRequestDatabase = mFirebaseDatabase.getReference("requests");
        mListener = (OnDataReadyListener) context;
        // TODO: try catch with throwable
    }

    /**
     * Attach listeners to Firebase to fetch request objects when changes occur in Firebase
     * database occur. Listeners are for all requests, nearby requests in the same city as the
     * user, requests created by the user, and requests claimed/completed by the user.
     * @param String userID   current user's FBID
     * @param String userCity the user's current city based on their Android location
     */
    public void attachFirebaseListeners(String userID, String userCity) {
        DatabaseReference requestDatabase = getRequestDatabase();

        // attach listener for all requests
        requestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> allRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("All requests", "requestID: " + newRequest.getRequestID());
                    allRequests.add(newRequest);
                }
                // save all requests
                RequestTracker.getInstance().setAllRequests(allRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // attach listener for nearby requests
        requestDatabase.orderByChild("requestData/restaurant/restaurantCity").equalTo(userCity)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> nearbyRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("Nearby requests", "requestID: " + newRequest.getRequestID());
                    nearbyRequests.add(newRequest);
                }
                // save nearby requests
                RequestTracker.getInstance().setNearbyRequests(nearbyRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // attach listener for user requests (requests created by the user)
        requestDatabase.orderByChild("requesterID").equalTo(userID)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> userRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("User requests", "requestID: " + newRequest.getRequestID());
                    userRequests.add(newRequest);
                }
                // save nearby requests
                RequestTracker.getInstance().setUserRequests(userRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // attach listener for user reservations (requests claimed/completed by the user)
        requestDatabase.orderByChild("acceptorID").equalTo(userID)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> userReservations = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("User reservations", "requestID: " + newRequest.getRequestID());
                    userReservations.add(newRequest);
                }
                // save nearby requests
                RequestTracker.getInstance().setUserReservations(userReservations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });
    }

    /**
     * Attach listeners to Firebase to fetch request objects during initial data pull from
     * Firebase database after user logs into the app upon startup. Listeners are for all
     * requests, nearby requests in the same city as the user, requests created by the user, and
     * requests claimed/completed by the user.
     * @param String userID   current user's FBID
     * @param String userCity the user's current city based on their Android location
     */
    public void attachInitialFirebaseListeners(String userID, String userCity) {
        DatabaseReference requestDatabase = getRequestDatabase();

        // listener to get all requests when app first starts
        requestDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> allRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("Init all requests", "requestID: " + newRequest.getRequestID());
                    allRequests.add(newRequest);
                }
                // save all requests
                RequestTracker.getInstance().setAllRequests(allRequests);
                mListener.onNearbyRequestsReady();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // listener to get nearby requests when app first starts
        requestDatabase.orderByChild("requestData/restaurant/restaurantCity").equalTo(userCity)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> nearbyRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("Init nearby requests", "requestID: " + newRequest.getRequestID());
                    nearbyRequests.add(newRequest);

                }
                // save nearby requests
                RequestTracker.getInstance().setNearbyRequests(nearbyRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // listener to get user's created requests when app first starts
        requestDatabase.orderByChild("requesterID").equalTo(userID)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> userRequests = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("Init user requests", "requestID: " + newRequest.getRequestID());
                    userRequests.add(newRequest);
                }
                // save nearby requests
                RequestTracker.getInstance().setUserRequests(userRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });

        // listener to get user's claimed/reserved when app first starts
        requestDatabase.orderByChild("reserverID").equalTo(userID)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                ArrayList<Request> userReservations = new ArrayList<Request>();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Request newRequest = parseJson(requestSnapshot);
                    Log.d("Init user reservations", "requestID: " + newRequest.getRequestID());
                    userReservations.add(newRequest);
                }
                // save nearby requests
                RequestTracker.getInstance().setUserReservations(userReservations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
            }
        });
    }

    /**
     * Parse JSON object from database into a Request object
     * @param  DataSnapshot JSON object read from Firebase
     * @return Request data contained in the JSON object
     */
    public Request parseJson(DataSnapshot requestSnapshot) {
        String requestID = (String) requestSnapshot.child("requestID").getValue();
        String requesterID = (String) requestSnapshot.child("requesterID").getValue();

        // request state info
        DataSnapshot requestState = requestSnapshot.child("requestState");
        String stateStr = (String) requestState.child("temp").getValue();

        // request info
        DataSnapshot requestData = requestSnapshot.child("requestData");

        String startTime = (String) requestData.child("startTime").getValue();
        String endTime = (String) requestData.child("endTime").getValue();
        String topic1 = (String) requestData.child("topic1").getValue();
        String topic2 = (String) requestData.child("topic2").getValue();

        // restaurant info
        DataSnapshot restaurant_info = requestData.child("restaurant");
        String restaurantID = (String) restaurant_info.child("restaurantID").getValue();
        String restaurantName = (String) restaurant_info.child("restaurantName").getValue();
        String restaurantPhoneNumber = (String) restaurant_info.child("restaurantPhoneNumber").getValue();
        String restaurantAddress = (String) restaurant_info.child("restaurantAddress").getValue();
        String restaurantCity = (String) restaurant_info.child("restaurantCity").getValue();
        Restaurant restaurant = new Restaurant(restaurantID, restaurantName, restaurantPhoneNumber, restaurantAddress, restaurantCity);

        // Create RequestData object
        RequestData newRequestData = new RequestData(startTime, endTime, topic1, topic2, restaurant);
        Request newRequest = new Request(requesterID, newRequestData, requestID);

        return newRequest;
    }

    /**
     * Return reference to the request node in Firebase
     * @return DatabaseReference request node reference
     */
    public DatabaseReference getRequestDatabase() {
        return mRequestDatabase;
    }

    /**
     * Return a new unique ID for a new request
     * @return String unique ID
     */
    public String getNewRequestID() {
        return mRequestDatabase.push().getKey();
    }

    /**
     * Write a request object to database
     * @param  Request request object to be written
     * @return boolean true if write succeeded
     */
    public boolean writeRequest(Request request) {
        mRequestDatabase.child(request.getRequestID()).setValue(request);
        Log.d("bahh", "wrote request");
        // wrote successfully to database
        return true;
        // TODO: error handling
    }

    public interface OnDataReadyListener {
        void onNearbyRequestsReady();
        void onRequesterRequestsReady();
        void onReserverRequestsReady();
    }

}