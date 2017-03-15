package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott on 3/13/2017.
 */
public class Heightmap {
    @SerializedName("model")
    public String model = "model_a";
    @SerializedName("wall_height")
    public String wallHeight = "1";
    @SerializedName("map")
    public String map = "";
}
