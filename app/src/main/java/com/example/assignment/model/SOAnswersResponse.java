package com.example.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SOAnswersResponse {



    private List<Item> items = null;


    public List<Item> getItems()
    {
        return items;
    }
}
