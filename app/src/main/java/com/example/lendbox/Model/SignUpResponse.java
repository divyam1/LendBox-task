package com.example.lendbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DU on 11/12/2017.
 */

public class SignUpResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    @SerializedName("error")
    private boolean error;

    public boolean getError() { return this.error; }

    public void setError(boolean error) { this.error = error; }

    @SerializedName("data")
    private ArrayList<String> data = new ArrayList<>();

    public ArrayList<String> getData() { return this.data; }

    public void setData(ArrayList<String> data) { this.data = data; }
}
