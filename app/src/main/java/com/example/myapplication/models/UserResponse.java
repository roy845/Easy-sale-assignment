package com.example.myapplication.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("data")
    private List<User> data;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("total")
    private int total;
    @SerializedName("per_page")
    private int per_page;

    public List<User> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return per_page;
    }

    public int getTotalPages() {
        return total_pages;
    }
}
