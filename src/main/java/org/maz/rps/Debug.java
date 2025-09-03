package org.maz.rps;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import javafx.geometry.Point2D;

import com.almasb.fxgl.entity.Entity;

public class Debug {

    public enum EntityType {
        DEBUG
    }

    public static Entity spawnDebugPoint(Point2D location) {
        return entityBuilder()
                .type(EntityType.DEBUG)
                .at(location)
                .viewWithBBox("debugPoint.png")
                .buildAndAttach();
    }
}
