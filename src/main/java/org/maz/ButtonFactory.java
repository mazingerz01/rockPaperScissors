package org.maz;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class ButtonFactory {
    public static Button createButton(Ikon iconCode, EventHandler<ActionEvent> eventHandler) {
        var button = new Button(null, new FontIcon(iconCode));
        button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.BUTTON_ICON);
        button.setOnAction(eventHandler);
        return button;
    }

    public static ToggleButton createToggleButton(Ikon iconCode, EventHandler<ActionEvent> eventHandler) {
        var button = new ToggleButton(null, new FontIcon(iconCode));
        button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.BUTTON_ICON);
        button.setOnAction(eventHandler);
        return button;
    }
}
