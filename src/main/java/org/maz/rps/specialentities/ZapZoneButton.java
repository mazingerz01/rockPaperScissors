package org.maz.rps.specialentities;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.util.Duration;

import atlantafx.base.theme.Styles;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.maz.rps.RPSApp;

public class ZapZoneButton extends Button {
    private boolean active = false;

    public ZapZoneButton() {
        setAlignment(Pos.CENTER);
        setTranslateX(10);
        FontIcon zap = new FontIcon(Feather.ZAP);
        setGraphic(zap);
        getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.BUTTON_ICON);
        setOnMouseClicked((event -> {
            if (active) {
                active=false;
                getGameScene().setCursor(Cursor.NONE);
                RPSApp.setZapZone(new ZapZone());
            }
        }));
        applyAnimations();
    }

    private void applyAnimations() {
        // Slowly make button visible
        setOpacity(0.4);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(15), this);
        translateTransition.setFromY(-50);
        translateTransition.setToY(0);
        translateTransition.setOnFinished((event) -> {
            setOpacity(1.0);
            // Pulse animation
            ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(0.45), this);
            pulseAnimation.setByX(1.4);
            pulseAnimation.setByY(1.4);
            pulseAnimation.setAutoReverse(true);
            pulseAnimation.setCycleCount(2);
            pulseAnimation.play();
            active = true;
        });
        translateTransition.play();
    }

    public void reset() {
        active = false;
        applyAnimations();
    }
}
