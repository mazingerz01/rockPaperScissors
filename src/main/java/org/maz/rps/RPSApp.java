package org.maz.rps;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicReference;

import atlantafx.base.theme.PrimerLight;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleSystem;
import org.maz.rps.components.FloatMoveComponent;
import org.maz.rps.components.MoveComponent;
import org.maz.rps.menu.RPSSceneFactory;
import org.maz.rps.specialentities.ZapZone;

public class RPSApp extends GameApplication {
    private static final Logger LOGGER = Logger.get(RPSApp.class);

    private static final double SCREEN_RATIO = 0.9;
    public static final String VERSION = "1.0";

    private static EntityType currentlySelected = EntityType.ROCK;
    private final EnumMap<EntityType, SimpleIntegerProperty> entityCounts = new EnumMap<>(EntityType.class);
    private final SimpleIntegerProperty totalCount = new SimpleIntegerProperty(0);
    private static final ParticleSystem particleSystem = new ParticleSystem();

    private static boolean killMode;
    private static boolean pauseMode;
    private static ZapZone zapZone;
    private static InGameUI inGameUI;

    //xxxxm tribuo:  csv   e.g. for 1 line: 200,34,23,23,rock

    private interface IEntity {
    }

    public enum EntityType implements IEntity {
        ROCK, PAPER, SCISSORS
    }

    public enum SpecialEntityType implements IEntity {
        ZAP_ZONE
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Rock Paper Scissors");
        settings.setVersion(VERSION);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(false);   // via menu?
        settings.setWidth((int) (screenSize.getWidth() * SCREEN_RATIO));
        settings.setHeight((int) (screenSize.getHeight() * SCREEN_RATIO));
        settings.setSceneFactory(new RPSSceneFactory());
        settings.setMainMenuEnabled(false);
        settings.setDeveloperMenuEnabled(false);

    }

    @Override
    protected void initGame() {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        currentlySelected = EntityType.values()[0];
        getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(getImagename(currentlySelected))));
        Arrays.stream(EntityType.values()).forEach(type -> entityCounts
                .put(type, new SimpleIntegerProperty(0)));
        getGameScene().addChild(particleSystem.getPane());
        particleSystem.getPane().setMouseTransparent(true);
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
        UserAction.addInputs(input);
    }

    @Override
    protected void initUI() {
        inGameUI = new InGameUI(entityCounts, totalCount);
        getGameScene().addUINode(inGameUI);
        getGameScene().setUIMouseTransparent(false);
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
        particleSystem.onUpdate(tpf);
    }

    static <E extends Enum<E> & IEntity> Entity spawnEntity(E entityType, Point2D... location) {
        EntityBuilder entityBuilder = entityBuilder()
                .type(entityType)
                .at(location.length == 1 ? location[0] : new Point2D(getInput().getMouseXWorld(), getInput().getMouseYWorld()))
                .collidable()
                .viewWithBBox(getImagename(entityType));
        Entity entity = entityBuilder.buildAndAttach();
        entity.addComponent(pauseMode ? new FloatMoveComponent() : new MoveComponent());
        return entity;
    }

    public static <E extends Enum<E> & IEntity> String getImagename(E entityType) {
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
        RPSApp.killMode = killMode;
    }

    public static boolean isKillMode() {
        return killMode;
    }

    public static boolean isPauseMode() {
        return pauseMode;
    }

    public static void setPauseMode(boolean pauseMode) {
        RPSApp.pauseMode = pauseMode;
    }

    public static void setZapZone(ZapZone entity) {
        RPSApp.zapZone = entity;
        if (entity != null) {
            getGameWorld().addEntity(entity);
        }
    }

    public static ZapZone getZapZone() {
        return zapZone;
    }

    public static ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public static EntityType getCurrentlySelected() {
        return currentlySelected;
    }

    public static void setCurrentlySelected(EntityType value) {
        currentlySelected = value;
    }

    public static InGameUI getInGameUI() {
        return inGameUI;
    }
}
