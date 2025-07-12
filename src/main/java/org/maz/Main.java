package org.maz;

import atlantafx.base.theme.PrimerLight;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.application.Application;
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
    private static final double SCREEN_RATIO = 0.7;
    private static final String VERSION = "0.1";

    private EntityType currentlySelected = EntityType.ROCK;
    private TimerAction spawnTimerAction;
    private final EnumMap<EntityType, SimpleIntegerProperty> entityCounts = new EnumMap<>(EntityType.class);
    private final SimpleIntegerProperty totalCount = new SimpleIntegerProperty(0);
    private static boolean killMode;
    private static boolean pauseMode;

    //xxxxm tribuo:  csv   e.g. for 1 line: 200,34,23,23,rock

    private interface IEntity {
    }

    public enum EntityType implements IEntity {
        ROCK, PAPER, SCISSORS
    }

    public enum SpecialEntityType implements IEntity {
        ZAP_ZONE
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

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Rock Paper Scissors");
        settings.setVersion(VERSION);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        settings.setFullScreenAllowed(true);
        // settings.setFullScreenFromStart(true);
        settings.setWidth((int) (screenSize.getWidth() * SCREEN_RATIO));
        settings.setHeight((int) (screenSize.getHeight() * SCREEN_RATIO));
    }

    @Override
    protected void initGame() {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        currentlySelected = EntityType.values()[0];
        getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(getImagename(currentlySelected))));
        Arrays.stream(EntityType.values()).forEach(type -> entityCounts
                .put(type, new SimpleIntegerProperty(0)));

        spawnTimerAction = getGameTimer().runAtInterval(() -> spawnEntity(currentlySelected), Duration.seconds(0.08));
        spawnTimerAction.pause();
    }

    @Override
    protected void initUI() {
        InGameUI inGameUI = new InGameUI(entityCounts, totalCount);
        getGameScene().addUINode(inGameUI);
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.ROCK, EntityType.PAPER, (r, p) -> {
            r.removeFromWorld();
            if (!killMode) {
                spawnEntity(EntityType.PAPER, r.getAnchoredPosition())
                        .addComponent(new ParticleComponent(CollisionEmitterFactory.getCollisionEmitter()));
            }
        });
        onCollisionBegin(EntityType.ROCK, EntityType.SCISSORS, (r, s) -> {
            s.removeFromWorld();
            if (!killMode) {
                spawnEntity(EntityType.ROCK, s.getAnchoredPosition())
                        .addComponent(new ParticleComponent(CollisionEmitterFactory.getCollisionEmitter()));
            }
        });
        onCollisionBegin(EntityType.PAPER, EntityType.SCISSORS, (p, s) -> {
            p.removeFromWorld();
            if (!killMode) {
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

    static <E extends Enum<E> & IEntity> Entity spawnEntity(E entityType, Point2D... location) {
        EntityBuilder entityBuilder = entityBuilder()
                .type(entityType)
                .at(location.length == 1 ? location[0] : new Point2D(getInput().getMouseXWorld(), getInput().getMouseYWorld()))
                .collidable();
        if (entityType instanceof EntityType) {
            entityBuilder.viewWithBBox(getImagename(entityType));
        } else {
            entityBuilder.viewWithBBox(new ZapZone());
        }
        Entity entity = entityBuilder.buildAndAttach();

        if (entityType != SpecialEntityType.ZAP_ZONE) {
            entity.addComponent(pauseMode ? new FloatMoveComponent() : new MoveComponent());
        }
        return entity;
    }

    private static <E extends Enum<E> & IEntity> String getImagename(E entityType) {
        return switch ((EntityType) entityType) {
            case ROCK -> "rock.png";
            case PAPER -> "paper.png";
            case SCISSORS -> "scissors.png";
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setKillMode(boolean killMode) {
        Main.killMode = killMode;
    }

    public static boolean isKillMode() {
        return killMode;
    }

    public static boolean isPauseMode() {
        return pauseMode;
    }

    public static void setPauseMode(boolean pauseMode) {
        Main.pauseMode = pauseMode;
    }
}
