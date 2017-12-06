package com.popcornchicken.tindine;

class RequestState {
    // not using enum because hard to use with Firebase
    static final String PENDING = "pending";
    static final String CLAIMED = "claimed";
    static final String COMPLETED = "completed";
}
