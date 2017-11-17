package com.popcornchicken.tindine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class RequestTracker {

    private static RequestTracker mRequestTracker = new RequestTracker();

    private User mCurrentUser;
    private ArrayList<Request> mAllRequests;
    private ArrayList<Request> mNearbyRequests;
    private ArrayList<Request> mUserRequests;
    private ArrayList<Request> mUserReservations;

    private RequestTracker() {
    }

    public static RequestTracker getInstance() {
        return mRequestTracker;
    }

    public ArrayList<Request> filterAllRequestsByCity(String userCity) {
        ArrayList<Request> filteredNearbyRequests = new ArrayList<Request>();
        for (Request request: mAllRequests) {
            // check if request is in the same city as the user
            if (userCity.equals(request.getRequestData().getRestaurant().getRestaurantCity())) {
                // add request to nearbyRequests
                filteredNearbyRequests.add(request);
            }
        }
        return filteredNearbyRequests;
    }

    public void setNearbyRequests(ArrayList<Request> nearbyRequests) {
        mNearbyRequests = nearbyRequests;
    }

    public void setAllRequests(ArrayList<Request> allRequests) {
        mAllRequests = allRequests;
    }

    public void setUserRequests(ArrayList<Request> userRequests) {
        mUserRequests = userRequests;
    }

    public void setUserReservations(ArrayList<Request> userReservations) {
        mUserReservations = userReservations;
    }

    public ArrayList<Request> getNearbyRequests() {
        return mNearbyRequests;
    }

    public ArrayList<Request> getAllRequests() { return mAllRequests; }

    public ArrayList<Request> getUserRequests() {
        return mUserRequests;
    }

    public ArrayList<Request> getUserReservations() {
        return mUserReservations;
    }

}
