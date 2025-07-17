package org.maz.zapzone;

import atlantafx.base.theme.Styles;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.maz.Main;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class ZapZoneButton extends Button {
    public ZapZoneButton() {
        setAlignment(Pos.CENTER);
        setTranslateX(10);
        FontIcon zap = new FontIcon(Feather.ZAP);
        setGraphic(zap);
        getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.BUTTON_ICON);
        setOnAction((event -> {
            getGameScene().setCursor(Cursor.NONE);
            Main.setZapZone(new ZapZone());
        }));
        applyAnimations();
        setOpacity(0.4);
    }

    private void applyAnimations() {//}, ImageView powerUpImageView) {
        // Slowly make powerup visible
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), this);  //xxxm set to 15sec
        translateTransition.setFromY(-50);
        translateTransition.setToY(0);
        //translateTransition.setFromX(1);
        //translateTransition.setToX(1);
        translateTransition.setOnFinished((event) -> {
            setOpacity(1.0);
            // Pulse animation
            ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(0.45), this);
            pulseAnimation.setByX(1.4);
            pulseAnimation.setByY(1.4);
            pulseAnimation.setAutoReverse(true);
            pulseAnimation.setCycleCount(2);
            pulseAnimation.play();
        });
        translateTransition.play();
    }
}
