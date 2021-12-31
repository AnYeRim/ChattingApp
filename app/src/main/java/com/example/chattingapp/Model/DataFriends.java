package com.example.chattingapp.Model;

public class DataFriends {

    private String imageURL, nikName, message;

    public DataFriends(String imageURL, String nikName, String message) {
        this.imageURL = imageURL;
        this.nikName = nikName;
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNikName() {
        return nikName;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
