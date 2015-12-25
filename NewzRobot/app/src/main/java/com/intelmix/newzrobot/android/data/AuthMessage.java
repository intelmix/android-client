package com.intelmix.newzrobot.android.data;

/**
 * Created by mahdi on 12/17/15.
 */
public class AuthMessage {
    private String token;

    public AuthMessage() {
    }

    public AuthMessage(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
