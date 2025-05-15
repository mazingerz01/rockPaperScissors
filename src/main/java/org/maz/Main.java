package org.maz;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.awt.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class Main extends GameApplication {
    private EntityType currentlySelected = EntityType.ROCK;
    private TimerAction spawnTimerAction;
    private TimerAction removePowerUpTimerAction;
    private final EnumMap<EntityType, SimpleIntegerProperty> entityCounts = new EnumMap<>(EntityType.class);
    private final SimpleIntegerProperty totalCount = new SimpleIntegerProperty(0);
    private static boolean killmode;
    private static boolean pauseMode;
    private static Entity powerUp;

    private interface IEntity {
    }

    public enum EntityType implements IEntity {
        ROCK, PAPER, SCISSORS
    }

    public enum SpecialEntityType implements IEntity {
        BLACK_HOLE, POWER_UP
    }

    UserAction mouseLeft = new UserAction("Mouse Left") {
        @Override
        protected void onActionBegin() {
            spawnEntity(currentlySelected);
            spawnTimerAction.resume();
        }

        @Override
        protected void onActionEnd() {
            spawnTimerAction.pause();
        }
    };

    UserAction mouseRight = new UserAction("Mouse Right") {
        @Override
        protected void onActionBegin() {
            int newIndex =
                    currentlySelected.ordinal() == EntityType.values().length - 1 ? 0 : currentlySelected.ordinal() + 1;
            currentlySelected = EntityType.values()[newIndex];
            getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(getImagename(currentlySelected))));
        }
    };

    UserAction keyCodeB = new UserAction("Key B") {
        @Override
        protected void onActionBegin() {
            //spawnEntity(BLACK_HOLE);
        }
    };

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Rock Paper Scissors");
        settings.setVersion("1.0");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);
        settings.setWidth((int) screenSize.getWidth());
        settings.setHeight((int) screenSize.getHeight());
    }

    @Override
    protected void initGame() {
        currentlySelected = EntityType.values()[0];
        getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(getImagename(currentlySelected))));
        Arrays.stream(EntityType.values()).forEach(type -> entityCounts
                .put(type, new SimpleIntegerProperty(0)));

        spawnTimerAction = getGameTimer().runAtInterval(() -> spawnEntity(currentlySelected), Duration.seconds(0.08));
        spawnTimerAction.pause();

        removePowerUpTimerAction = getGameTimer().runAtInterval(() -> {
            if (powerUp != null) {
                getGameWorld().removeEntity(powerUp);
                powerUp = null;
            }
        }, Duration.seconds(8));
    }

    @Override
    protected void initUI() {
        InGameUI inGameUI = new InGameUI(entityCounts, totalCount);
        getGameScene().addUINode(inGameUI);
        inGameUI.getKillModeButton().setOnAction(e -> killmode = !killmode);
        inGameUI.getPlayPauseButton().setOnAction(e -> {
            if (pauseMode) {
                getGameWorld().getEntities().forEach(entity -> {
                    entity.removeComponent(FloatMoveComponent.class);
                    entity.addComponent(new MoveComponent());
                });
            } else {
                getGameWorld().getEntities().forEach(entity -> {
                    entity.removeComponent(MoveComponent.class);
                    entity.addComponent(new FloatMoveComponent());
                });
            }
            pauseMode = !pauseMode;
        });
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.ROCK, EntityType.PAPER, (r, p) -> {
            r.removeFromWorld();
            if (!killmode) {
                spawnEntity(EntityType.PAPER, r.getAnchoredPosition())
                        .addComponent(new ParticleComponent(CollisionEmitterFactory.getCollisionEmitter()));
            }
        });
        onCollisionBegin(EntityType.ROCK, EntityType.SCISSORS, (r, s) -> {
            s.removeFromWorld();
            if (!killmode) {
                spawnEntity(EntityType.ROCK, s.getAnchoredPosition())
                        .addComponent(new ParticleComponent(CollisionEmitterFactory.getCollisionEmitter()));
            }
        });
        onCollisionBegin(EntityType.PAPER, EntityType.SCISSORS, (p, s) -> {
            p.removeFromWorld();
            if (!killmode) {
                spawnEntity(EntityType.SCISSORS, p.getAnchoredPosition())
                        .addComponent(new ParticleComponent(CollisionEmitterFactory.getCollisionEmitter()));
            }
        });
    }

    @Override
    protected void initInput() {
        Input input = getInput();
        input.addAction(mouseLeft, MouseButton.PRIMARY);
        input.addAction(mouseRight, MouseButton.SECONDARY);
    }

    @Override
    protected void onUpdate(double tpf) {
        AtomicReference<Integer> total = new AtomicReference<>(0);
        Arrays.stream(EntityType.values()).forEach(type -> {
            int n = getGameWorld().getEntitiesByType(type).size();
            entityCounts.get(type).set(n);
            total.updateAndGet(v -> v + n);
        });
        totalCount.set(total.get());
    }

    private <E extends Enum<E> & IEntity> Entity spawnEntity(E entityType, Point2D... location) {
        String texture = getImagename(entityType);
        Entity entity = entityBuilder()
                .type(entityType)
                .at(location.length == 1 ? location[0] : new Point2D(getInput().getMouseXWorld(), getInput().getMouseYWorld()))
                .viewWithBBox(texture)
                .collidable()
                .buildAndAttach();
        if (entityType != SpecialEntityType.POWER_UP) {
            entity.addComponent(pauseMode ? new FloatMoveComponent() : new MoveComponent());
        }
        return entity;
    }

    private <E extends Enum<E> & IEntity> String getImagename(E entityType) {
        if (entityType instanceof EntityType) {
            return switch ((EntityType) entityType) {
                case ROCK -> "rock.png";
                case PAPER -> "paper.png";
                case SCISSORS -> "scissors.png";
            };
        } else {
            return switch ((SpecialEntityType) entityType) {
                case BLACK_HOLE -> "blackhole.png";
                case POWER_UP -> "powerup.png";
            };
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
