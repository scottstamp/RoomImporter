package com.hypermine.arcturus.roomimporter.handlers;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomLayout;
import com.eu.habbo.habbohotel.rooms.RoomManager;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboManager;
import com.hypermine.arcturus.roomimporter.payloads.FloorItem;
import com.hypermine.arcturus.roomimporter.payloads.RoomData;
import com.hypermine.arcturus.roomimporter.payloads.WallItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by scott on 3/14/2017.
 */
public class ImportHandler {
    public static void handle (RoomData roomData)
    {
        try { ImportRoomData(roomData); }
        catch (SQLException exception) {
            Emulator.getLogging().logErrorLine(exception.getMessage());
        }
    }

    public static void ImportRoomData(RoomData roomData) throws SQLException
    {
        RoomManager roomManager = Emulator.getGameEnvironment().getRoomManager();
        HabboManager habboManager = Emulator.getGameEnvironment().getHabboManager();


        PreparedStatement statement = Emulator.getDatabase().prepare("SELECT auth_ticket FROM users WHERE id = ? LIMIT 1");
        Emulator.getLogging().logDebugLine("[RoomImporter] ownerId: " + roomData.ownerId);
        statement.setInt(1, Integer.parseInt(roomData.ownerId));

        ResultSet s = statement.executeQuery();
        if (s.next()) {
            Habbo roomOwner = habboManager.loadHabbo(s.getString("auth_ticket"));

            Room room;

            Emulator.getLogging().logDebugLine("[RoomImporter] - owner: " + roomOwner.getHabboInfo().getUsername());
            room = roomManager.createRoom(roomOwner,
                    roomData.roomInfo.name,
                    roomData.roomInfo.description,
                    roomData.heightmap.model,
                    Integer.parseInt(roomData.roomInfo.maxUsers),
                    0);

            room.setScore(roomData.roomInfo.score);
            room.setBackgroundPaint(roomData.paint.landscape);
            room.setFloorPaint(roomData.paint.floor);
            room.setWallPaint(roomData.paint.wallpaper);

            if (room.getId() > -1) {
                ImportRoomHeightmap(roomData, room);
                ImportFloorItems(roomData, roomOwner, room);
                ImportWallItems(roomData, roomOwner, room);
            }

            room.save();
        }
        s.close();
        statement.close();
        statement.getConnection().close();
    }

    public static void ImportRoomHeightmap(RoomData roomData, Room room) {
        RoomLayout layout = Emulator.getGameEnvironment().getRoomManager()
                .insertCustomLayout(room, roomData.heightmap.map, 0, 0, 0);

        room.setHasCustomLayout(true);
        room.setNeedsUpdate(true);
        room.setLayout(layout);
        room.setWallSize(1);
        room.setFloorSize(1);
        room.setWallHeight(Integer.parseInt(roomData.heightmap.wallHeight));
        room.save();
    }

    public static void ImportWallItems(RoomData roomData, Habbo roomOwner, Room room) throws SQLException {
        for (WallItem wallItem : roomData.wallItems) {
            int id = 0;
            PreparedStatement e = Emulator.getDatabase().prepare(
                    "INSERT INTO items (user_id, room_id, item_id, wall_pos, extra_data)" +
                            "VALUES (?, ?, (SELECT id FROM items_base WHERE item_name = ? LIMIT 1), ?, ?)"
            );

            e.setInt(1, roomOwner.getHabboInfo().getId());
            e.setInt(2, room.getId());
            e.setString(3, wallItem.spriteName);
            e.setString(4, wallItem.position);
            if (wallItem.extradata.contains("/:!"))
                wallItem.extradata = wallItem.extradata.replace("/:!", (char)9 + "");
            e.setString(5, wallItem.extradata);

            e.execute();
            ResultSet set = e.getGeneratedKeys();
            if (set.next()) {
                id = set.getInt(1);
            }

            Emulator.getLogging().logDebugLine("[RoomImporter] Wall Item ID: " + id);

            e.close();
            e.getConnection().close();
        }
    }

    public static void ImportFloorItems(RoomData roomData, Habbo roomOwner, Room room) throws SQLException {
        for (FloorItem floorItem : roomData.floorItems) {
            int id = 0;
            PreparedStatement e = Emulator.getDatabase()
                    .prepare("INSERT INTO items (user_id, room_id, item_id, x, y, z, rot, extra_data) " +
                            "VALUES (?, ?, (SELECT id FROM items_base WHERE item_name = ? LIMIT 1), ?, ?, ?, ?, ?)");
            e.setInt(1, roomOwner.getHabboInfo().getId());
            e.setInt(2, room.getId());
            e.setString(3, floorItem.itemName);
            e.setInt(4, floorItem.x);
            e.setInt(5, floorItem.y);
            e.setString(6, floorItem.height);
            e.setInt(7, floorItem.rotation);
            if (floorItem.extradata.contains("/:!"))
                floorItem.extradata = floorItem.extradata.replace("/:!", (char)9 + "");
            e.setString(8, floorItem.extradata);
            e.execute();
            ResultSet set = e.getGeneratedKeys();
            if (set.next()) {
                id = set.getInt(1);
            }

            Emulator.getLogging().logDebugLine("[RoomImporter] Floor Item ID: " + id);

            set.close();
            e.close();
            e.getConnection().close();
        }
    }
}
