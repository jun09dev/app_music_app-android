package com.example.app.dto;


import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class LoginResponse {
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("accessToken")
    private String accessToken;

    private Map<String, Object> user;

    public String getMessage() { return message; }
    public String getToken() { return token != null ? token : accessToken; }
    public Map<String, Object> getUser() { return user; }
}
