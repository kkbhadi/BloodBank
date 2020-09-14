package com.bloodbankapp.firebase;


public interface FirebaseNetworkCallback {
    void onSuccess(Object data);

    void onFailure(String error);
}
