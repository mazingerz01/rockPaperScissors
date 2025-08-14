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
    public static final double RADIUS = 100.0;

    public ZapZone() {
        this.setType(RPSApp.SpecialEntityType.ZAP_ZONE);
        this.setPosition(getInput().getMouseXWorld(), getInput().getMouseYWorld());
        this.getViewComponent().addChild(getCircleShape());
        this.addComponent(new RotateComponent());

        this.xProperty().bind(getGameScene().getInput().mouseXWorldProperty());
        this.yProperty().bind(getGameScene().getInput().mouseYWorldProperty());
    }

    private Circle getCircleShape() {
        Circle circle = new Circle(RADIUS);
        circle.setStroke(Color.MAGENTA);
        circle.setFill(new BackgroundFill(new Color(0.99, 0.28, 0.75, 0.15), null, null).getFill());
        circle.setStrokeWidth(3);
        circle.getStrokeDashArray().addAll(14.0, 24.0);
        circle.setStrokeLineCap(StrokeLineCap.ROUND);
        return circle;
    }

}
