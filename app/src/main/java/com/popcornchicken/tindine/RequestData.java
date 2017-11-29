package com.popcornchicken.tindine;

import java.util.Date;
import java.util.Calendar;

public class RequestData {

    private String mStartTime;
    private String mEndTime;
    private String mTopic1;
    private String mTopic2;
    private Restaurant mRestaurant;

    /**
     * Creates a RequestData instance.
     */
    public RequestData() {
    }

    /**
     * Creates a RequestData instance.
     * @param startTime The earliest time the requester would like the reservation to be placed.
     * @param endTime The time at which the request is cancelled if it has not been completed.
     * @param topic1 Lunch topic 1.
     * @param topic2 Lunch topic 2.
     * @param restaurant Restaurant object containing requested restaurant information.
     */
    public RequestData(String startTime, String endTime, String topic1, String topic2,
                       Restaurant restaurant) {

        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mTopic1 = topic1;
        this.mTopic2 = topic2;
        this.mRestaurant = restaurant;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    public String getTopic1() {
        return mTopic1;
    }

    public void setTopic1(String topic1) {
        this.mTopic1 = topic1;
    }

    public String getTopic2() {
        return mTopic2;
    }

    public void setmTopic2(String topic2) {
        this.mTopic2 = topic2;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.mRestaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }
}
