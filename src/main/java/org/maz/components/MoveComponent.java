package org.maz.components;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

import javafx.geometry.Point2D;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

public class MoveComponent extends Component {
    private Point2D start;
    private Point2D target;
    private Point2D controlPoint1;
    private double t = 0;
    private final double speed = FXGLMath.random(0.02, 0.10);

    @Override
    public void onAdded() {
        selectNewTarget();
    }

    @Override
    public void onUpdate(double tpf) {
        t += (speed * tpf);
        Point2D newPos = FXGLMath.bezier(start, controlPoint1, target, t);
        entity.setPosition(newPos);
        if (newPos.getX() < 0 || newPos.getX() > getGameScene().getWidth() || newPos.getY() < 0 || newPos.getY() > getGameScene().getHeight()) {
            selectNewTarget();
        }
    }

    private void selectNewTarget() {
        t = 0;
        start = entity.getPosition();
        target = new Point2D(FXGLMath.random(0, getGameScene().getWidth()), FXGLMath.random(0, getGameScene().getHeight()));
        controlPoint1 = new Point2D(FXGLMath.random(0, getGameScene().getWidth()), FXGLMath.random(0, getGameScene().getHeight()));
    }
}
