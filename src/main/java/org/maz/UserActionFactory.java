package org.maz;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.util.Duration;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class UserActionFactory {
    private static TimerAction spawnTimerAction;

    public static UserAction createMouseLeft() {
        return new UserAction("mouse left") {
            @Override
            protected void onActionBegin() {
                if (Main.getZapZone() != null) {
                    List<Entity> removeList = getGameWorld().getEntities().stream().filter(ent -> ent.distanceBBox(Main.getZapZone()) < 100).toList();
                    getGameWorld().removeEntities(removeList);
                    getGameWorld().removeEntity(Main.getZapZone());
                    Main.setZapZone(null);
                    getGameScene().setCursor(Cursor.DEFAULT);
                } else {
                    // There is no possibility of resetting the timer, so create a new one
                    spawnTimerAction = getGameTimer().runAtInterval(() -> Main.spawnEntity(Main.getCurrentlySelected()), Duration.seconds(0.08));
                }
            }

            @Override
            protected void onActionEnd() {
                spawnTimerAction.expire();
            }
        };

    }

    public static UserAction createMouseRight() {
        return new UserAction("mouse right") {
            @Override
            protected void onActionBegin() {
                int newIndex =
                        Main.getCurrentlySelected().ordinal() == Main.EntityType.values().length - 1 ? 0 : Main.getCurrentlySelected().ordinal() + 1;
                Main.setCurrentlySelected(Main.EntityType.values()[newIndex]);
                getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(Main.getImagename(Main.getCurrentlySelected()))));
            }
        };
    }
}
