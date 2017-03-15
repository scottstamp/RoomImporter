package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott on 3/13/2017.
 */
public class FloorItem {
    @SerializedName("base_id")
    public String baseId = null;
    @SerializedName("x")
    public int x = 0;
    @SerializedName("y")
    public int y = 0;
    @SerializedName("height")
    public String height = "0.0";
    @SerializedName("rotation")
    public int rotation = 0;
    @SerializedName("extradata")
    public String extradata = null;
    @SerializedName("owner_id")
    public String ownerId = null;
    @SerializedName("room_id")
    public String roomId = null;
    @SerializedName("interaction_type")
    public String interactionType = null;
    @SerializedName("item_name")
    public String itemName = null;
}
