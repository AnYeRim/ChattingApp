package com.example.chattingapp.Model;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClient {
    private static final String SOCKET_URI = "http://15.164.191.17:3000/";
    private static final String[] TRANSPORTS = {
            "websocket"
    };
    private static Socket instance;

    public static Socket getInstance() {
        if (instance == null) {
            try {
                final IO.Options options = new IO.Options();
                options.transports = TRANSPORTS;
                instance = IO.socket(SOCKET_URI, options);
            } catch (final URISyntaxException e) {
                Log.e("Tag", e.toString());
            }
        }
        return instance;
    }
}