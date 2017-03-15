package com.hypermine.arcturus.roomimporter;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.HabboPlugin;
import com.hypermine.arcturus.roomimporter.networking.RoomServer;

//import static spark.Spark.stop;

/**
 * Created by scott on 3/13/2017.
 */
public class RoomImporterPlugin extends HabboPlugin {

    private RoomServer roomServer;

    public RoomImporterPlugin() {}

    @Override
    public void onEnable() {
        Emulator.getLogging().logStart("[RoomImporter] YOLO! Good luck!");
        try {
            roomServer = new RoomServer("0.0.0.0", 3203);
            roomServer.initialize();
            roomServer.connect();
        } catch (Exception ex) {
            Emulator.getLogging().logErrorLine("[RoomImporter] server failed to start.");
        }
    }

    @Override
    public void onDisable() {
        roomServer.stop();
        Emulator.getLogging().logShutdownLine("[RoomImporter] Stopping RoomImporter Server!");
    }

    @Override
    public boolean hasPermission(Habbo habbo, String s) { return true; }
}
