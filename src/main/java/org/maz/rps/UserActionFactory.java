package org.maz.rps;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

import javafx.application.Platform;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.time.TimerAction;
import org.maz.rps.specialentities.ZapZone;

public class UserActionFactory {
    private static final Logger LOGGER = Logger.get("xxxm");
    private static TimerAction spawnTimerAction;

    public static void addInputs(Input input) {
        input.addAction(new UserAction("mouse left") {
            @Override
            protected void onActionBegin() {
                if (RPSApp.getZapZone() != null) {
                    List<Entity> removeList = getGameWorld().getEntities().stream().filter(ent -> ent.distanceBBox(RPSApp.getZapZone()) < ZapZone.RADIUS).toList();
                    // Entitylist for removal also contains zapZone.
                    removeList.forEach(e -> {
                        if (e.getType() != RPSApp.SpecialEntityType.ZAP_ZONE) {
                            ParticleEmitter emitter = CollisionEmitterFactory.getCollisionEmitter();
                            RPSApp.getParticleSystem().addParticleEmitter(emitter, e.getX(), e.getY());
                        }
                    });
                    Platform.runLater(() -> {
                        // onActionEnd will not be triggered if runLater is not used. https://github.com/AlmasB/FXGL/discussions/1426
                        getGameWorld().removeEntities(removeList);
                        RPSApp.setZapZone(null);
                    });
                    getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(RPSApp.getImagename(RPSApp.getCurrentlySelected()))));
                }
                else {
                    // There is no possibility of resetting the timer, so create a new one
                    spawnTimerAction = FXGLForKtKt.getGameTimer().runAtInterval(() -> RPSApp.spawnEntity(RPSApp.getCurrentlySelected()), Duration.seconds(0.08));
                }
            }

            @Override
            protected void onActionEnd() {
                if (RPSApp.getZapZone() == null) {
                    spawnTimerAction.expire();
                }
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("mouse right") {
            @Override
            protected void onActionBegin() {
                if (RPSApp.getZapZone() == null) {
                    int newIndex =
                            RPSApp.getCurrentlySelected().ordinal() == RPSApp.EntityType.values().length - 1 ? 0 : RPSApp.getCurrentlySelected().ordinal() + 1;
                    RPSApp.setCurrentlySelected(RPSApp.EntityType.values()[newIndex]);
                    getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(RPSApp.getImagename(RPSApp.getCurrentlySelected()))));
                }
            }
        }, MouseButton.SECONDARY);
    }

//    public static UserAction createMouseWheel() {
//        return new UserAction("mouse wheel") {
//            @Override
//                    protected void onScrollUp
//            input.addEventHandler(ScrollEvent.SCROLL, event -> {
//                double deltaY = event.getDeltaY();
//
//                if (deltaY > 0) {
//                    onScrollUp();
//                } else {
//                    onScrollDown();
//                }
//            });
//        }
//    }
}
