package com.example.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("high")
    @Expose
    private High high;

    public High getHigh() {
        return high;
    }


}

