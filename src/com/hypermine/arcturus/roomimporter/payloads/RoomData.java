package com.hypermine.arcturus.roomimporter.payloads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott on 3/13/2017.
 */
public class RoomData {
    @SerializedName("owner_id")
    public String ownerId = "0";
    @SerializedName("room_id")
    public String roomId = "0";
    @SerializedName("room_info")
    public RoomInfo roomInfo = new RoomInfo();
    @SerializedName("heightmap")
    public Heightmap heightmap = new Heightmap();
    @SerializedName("paint")
    public Paint paint = new Paint();
    @SerializedName("floor_items")
    public List<FloorItem> floorItems = null;
    @SerializedName("wall_items")
    public List<WallItem> wallItems = null;
}
