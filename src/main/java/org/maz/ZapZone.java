package org.maz;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

public class ZapZone extends Circle {
    public ZapZone() {
        setRadius(100.0);
        setStroke(Color.BLACK);
        setFill(Color.TRANSPARENT);
        setStrokeWidth(4);
        getStrokeDashArray().addAll(14.0, 24.0);
        setStrokeLineCap(StrokeLineCap.ROUND);
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
