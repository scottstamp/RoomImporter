package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott on 3/13/2017.
 */
public class RoomInfo {
    @SerializedName("name")
    public String name = null;
    @SerializedName("state")
    public String state = null;
    @SerializedName("max_users")
    public String maxUsers = null;
    @SerializedName("description")
    public String description = null;
    @SerializedName("score")
    public int score = 0;
    @SerializedName("category")
    public int category = 0;
    @SerializedName("tags")
    public List<String> tags = null;
}
