package com.example.chattingapp.Model;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;

public class SocketClient {
    private static final String TAG = SocketClient.class.getSimpleName();
    private static final String SOCKET_URI = "http://15.164.191.17:3000/";
    //private static final String SOCKET_PATH = "/your_path";
    private static final String[] TRANSPORTS = {
            "websocket"
    };
    private static io.socket.client.Socket instance;

    public static io.socket.client.Socket getInstance() {
        if (instance == null) {
            try {
                final IO.Options options = new IO.Options();
                //options.path = SOCKET_PATH;
                options.transports = TRANSPORTS;
                instance = IO.socket(SOCKET_URI, options);
            } catch (final URISyntaxException e) {
                Log.e(TAG, e.toString());
            }
        }
        return instance;
    }
}