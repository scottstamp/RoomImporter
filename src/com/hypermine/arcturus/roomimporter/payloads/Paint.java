package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott on 3/13/2017.
 */
public class Paint {
    @SerializedName("wallpaper")
    public String wallpaper = "0.0";
    @SerializedName("floor")
    public String floor = "0.0";
    @SerializedName("landscape")
    public String landscape = "0.0";
}
