package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott on 3/13/2017.
 */
public class WallItem {
    @SerializedName("sprite_id")
    public String spriteId = null;
    @SerializedName("sprite_name")
    public String spriteName = null;
    @SerializedName("position")
    public String position = null;
    @SerializedName("extradata")
    public String extradata = null;
    @SerializedName("user_id")
    public String userId = null;
    @SerializedName("state")
    public String state = null;
}
