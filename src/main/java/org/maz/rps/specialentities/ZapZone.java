package org.maz.rps.specialentities;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

import com.almasb.fxgl.entity.Entity;
import org.maz.rps.RPSApp;
import org.maz.rps.components.RotateComponent;

public class ZapZone extends Entity {
    private double radius = 100.0;
    private static final double RADIUS_STEP = 10;
    private static final double RADIUS_MIN = 30;
    private static final double RADIUS_MAX = 190;

    public ZapZone() {
        this.setType(RPSApp.SpecialEntityType.ZAP_ZONE);
        this.setPosition(getInput().getMouseXWorld(), getInput().getMouseYWorld());
        this.getViewComponent().addChild(getCircleShape());
        this.addComponent(new RotateComponent());

        this.xProperty().bind(getGameScene().getInput().mouseXWorldProperty());
        this.yProperty().bind(getGameScene().getInput().mouseYWorldProperty());
    }

    private Circle getCircleShape() {
        Circle circle = new Circle(radius);
        circle.setStroke(Color.MAGENTA);
        circle.setFill(new BackgroundFill(new Color(0.99, 0.28, 0.75, 0.15), null, null).getFill());
        circle.setStrokeWidth(3);
        circle.getStrokeDashArray().addAll(14.0, 24.0);
        circle.setStrokeLineCap(StrokeLineCap.ROUND);
        return circle;
    }

    public double getRadius() {
        return radius;
    }

    public void changeRadius(boolean grow) {
        radius += grow ? radius + RADIUS_STEP <= RADIUS_MAX ? +RADIUS_STEP : 0 : radius - RADIUS_STEP >= RADIUS_MIN ? -RADIUS_STEP : 0;
        this.getViewComponent().getChild(0, Circle.class).setRadius(radius);
    }
}
