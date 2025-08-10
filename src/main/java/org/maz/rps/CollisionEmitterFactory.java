package org.maz.rps;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import com.almasb.fxgl.particle.ParticleEmitter;

public class CollisionEmitterFactory {
    public static ParticleEmitter getCollisionEmitter() {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.setMaxEmissions(1);
        emitter.setNumParticles(1);
        emitter.setEmissionRate(0.56);
        emitter.setSize(5, 10);
        emitter.setScaleFunction(i -> new Point2D(0.6, 0.6));
        emitter.setExpireFunction(i -> Duration.seconds(0.5));
        emitter.setAccelerationFunction(() -> Point2D.ZERO);
        emitter.setVelocityFunction(i -> Point2D.ZERO);
        emitter.setStartColor(Color.ORANGE);
        emitter.setEndColor(Color.DARKRED);
        emitter.setBlendMode(BlendMode.SRC_ATOP);
        return emitter;
    }
}
