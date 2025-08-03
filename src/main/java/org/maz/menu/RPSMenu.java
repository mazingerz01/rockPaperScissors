package org.maz.menu;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import atlantafx.base.theme.Styles;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import org.maz.Main;

public class RPSMenu extends FXGLMenu {

    public RPSMenu() {
        super(MenuType.GAME_MENU);
        getContentRoot().setPrefWidth(FXGL.getAppWidth());
        getContentRoot().setPrefHeight(FXGL.getAppHeight());
        getContentRoot().setCursor(Cursor.DEFAULT);

        Text credits1 = FXGL.getUIFactoryService().newText("Rock Paper Scissors " + Main.VERSION, Color.WHITE, FontType.GAME, 24.0);
        Text credits2 = FXGL.getUIFactoryService().newText("Created by mazingerz01 - okifenoki@mailbox.org", Color.WHITE, 18);
        Text credits3 = FXGL.getUIFactoryService().newText("""
                A big thank you to the following people/libraries:
                * FXGL - JavaFX Game Library (MIT License), Copyright (c) AlmasB (almaslvl@gmail.com)
                """, Color.WHITE, 18);

        var butResume = getButton("Resume", e -> {
            getGameScene().setCursor(new ImageCursor(FXGL.getAssetLoader().loadImage(Main.getImagename(Main.getCurrentlySelected()))));
            fireResume();
        });

        var butExit = getButton("Exit game", e -> fireExit());

        Text help = FXGL.getUIFactoryService().newText("""
                Help:
                * Try to figure out what the buttons do
                * Try using the mouse wheel during "zapZone-mode"
                """, Color.WHITE, 18);

        var container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(60);
        container.layoutXProperty().bind(getContentRoot().widthProperty().subtract(container.widthProperty()).divide(2));
        container.getChildren().addAll(getSpacer(), credits1, credits2, credits3, butResume, butExit, getSpacer(), help);

        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        stackPane.setOpacity(0.8);
        stackPane.setPrefWidth(FXGL.getAppWidth());
        stackPane.setPrefHeight(FXGL.getAppHeight());

        getContentRoot().getChildren().addAll(stackPane, container);
    }

    private Button getButton(String text, EventHandler<ActionEvent> actionEvent) {
        var button = new Button(text, null);
        button.setPrefWidth(150);
        button.getStyleClass().addAll(Styles.LARGE, Styles.BUTTON_OUTLINED, Styles.ACCENT);
        button.setOnAction(actionEvent);
        return button;
    }

    private Region getSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

}
