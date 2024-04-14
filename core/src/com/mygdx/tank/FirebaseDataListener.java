package com.mygdx.tank;

public interface FirebaseDataListener {

    void onDataReceived(Object data);

    void onError(String errorMessage);
}
