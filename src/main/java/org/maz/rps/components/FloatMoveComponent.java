package org.maz.rps.components;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

import javafx.geometry.Point2D;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

public class FloatMoveComponent extends Component {

    private Point2D start;
    private Point2D target;
    private Point2D controlPoint1;
    private double t = 0;
    private final static double SPEED = 0.07;
    private final static double MOVE_RADIUS = 40;

    @Override
    public void onAdded() {
        selectNewTarget();
    }

    @Override
    public void onUpdate(double tpf) {
        t += tpf;
        Point2D newPos = FXGLMath.bezier(start, controlPoint1, target, t * SPEED);
        entity.setPosition(newPos);
        if (t > 1.2 || newPos.getX() < 0 || newPos.getX() > getGameScene().getWidth() || newPos.getY() < 0 || newPos.getY() > getGameScene().getHeight()) {
            selectNewTarget();
        }
    }

    private void selectNewTarget() {
        t = 0;
        start = entity.getPosition();
        target = new Point2D(FXGLMath.random(entity.getX() - MOVE_RADIUS, entity.getX() + MOVE_RADIUS), FXGLMath.random(entity.getY() - MOVE_RADIUS, entity.getY() + MOVE_RADIUS));
        controlPoint1 = new Point2D(FXGLMath.random(entity.getX() - MOVE_RADIUS, entity.getX() + MOVE_RADIUS), FXGLMath.random(entity.getY() - MOVE_RADIUS, entity.getY() + MOVE_RADIUS));
    }

}
