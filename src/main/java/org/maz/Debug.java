package org.maz;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

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
