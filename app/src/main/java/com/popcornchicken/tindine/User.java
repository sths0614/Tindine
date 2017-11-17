package com.popcornchicken.tindine;

import android.location.Location;
import java.util.ArrayList;
import java.util.List;
import com.facebook.Profile;

/**
 * Represents a user.
 */
public class User {

    //private String mName;
    //private Location mCurrentLocation;
    //private double mRating;
    private List<Request> mRequests;
    private List<Request> mReservations;

/**
 * Creates a User instance.
 */
    public User()
    {
        //mName = name;
        //mCurrentLocation = currentLocation;
        //mRating = rating;                           //set rating to 0?
        mRequests = new ArrayList<Request>();
        mReservations = new ArrayList<Request>();
    }

    /*
    public String getName()
    {
        return mName;
    }
    */

    /*
    public Location getCurrentLocation()
    {
        return mCurrentLocation;
    }
    */

    /*public double getRating()
    {
        return mRating;
    }*/

    /**
     * Adds the specified request to the user's list of requests.
     * @param request The request a user would like to place.
     * @return Return true when request is added.
     */
    public boolean addRequest(Request request)
    {
        mRequests.add(request);
        return true;
    }

    /**
     * Removes the specified request from the user's list of requests.
     * @param request The request a user would like to cancel.
     * @return If request exists in the user's list of requests, remove the request and return true. Otherwise, return false.
     */
    public boolean removeRequest(Request request)
    {
        if (mRequests.contains(request))
        {
            mRequests.remove(request);
            return true;
        }
        return false;
    }

    public List<Request> getRequests()
    {
        return mRequests;
    }

    /**
     * Adds the specified reservation to the user's list of reservations.
     * @param reservation The request a user would like to fulfill.
     * @return Return true when reservation is added.
     */
    public boolean addReservation(Request reservation)
    {
        mReservations.add(reservation);
        return true;
    }

    /**
     * Removes the specified reservation from the user's list of reservations.
     * @param reservation The request a user no longer wants to fulfill.
     * @return If reservation exists in the user's list of reservations, remove the reservation and return true. Otherwise, return false.
     */
    public boolean removeReservation(Request reservation)
    {
        if (mReservations.contains(reservation))
        {
            mReservations.remove(reservation);
            return true;
        }
        return false;
    }

    public List<Request> getReservations()
    {
        return mReservations;
    }

    //Implement Rating Feature for Part C
    /*public boolean updateRating(double rating)
    {
        return true;
    }*/

    public static String getUserFBID() {
        return Profile.getCurrentProfile().getId();
    }
}


//4 FUNCTIONS that add/remove requests/reservations: add firebase