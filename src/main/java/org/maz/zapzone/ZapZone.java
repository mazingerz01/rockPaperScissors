package org.maz.zapzone;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import org.maz.Main;
import org.maz.components.RotateComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

public class ZapZone extends Entity {
    public ZapZone() {
        this.setType(Main.SpecialEntityType.ZAP_ZONE);
        this.setPosition(getInput().getMouseXWorld(), getInput().getMouseYWorld());
        this.getViewComponent().addChild(getCircleShape());
        this.addComponent(new RotateComponent());
        // entity.addComponent(new BoundingBoxComponent());
        // entity.collidable()
        Main.setZapZone(true);
        this.xProperty().bind(getGameScene().getInput().mouseXWorldProperty());
        this.yProperty().bind(getGameScene().getInput().mouseYWorldProperty());
    }

    private Circle getCircleShape() {
        Circle circle = new Circle(100.0);
        circle.setRadius(100.0);
        circle.setStroke(Color.MAGENTA);
        circle.setFill(new BackgroundFill(new Color(0.99, 0.28, 0.75, 0.15), null, null).getFill());
        circle.setStrokeWidth(3);
        circle.getStrokeDashArray().addAll(14.0, 24.0);
        circle.setStrokeLineCap(StrokeLineCap.ROUND);
        return circle;
    }


    /*xxxm
    static     TimerAction removeZapZoneTimerAction = getGameTimer().runAtInterval(() -> {
        if (zapZone != null) {
            getGameWorld().removeEntity(zapZone);
            zapZone = null;
        }
    }, Duration.seconds(8));

 */

}
