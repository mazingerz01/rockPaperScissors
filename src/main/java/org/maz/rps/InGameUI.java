package org.maz.rps;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.EnumMap;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.maz.rps.components.FloatMoveComponent;
import org.maz.rps.components.MoveComponent;
import org.maz.rps.specialentities.ZapZoneButton;

public class InGameUI extends BorderPane {
    private ZapZoneButton zapZoneButton = new ZapZoneButton();

    InGameUI(EnumMap<RPSApp.EntityType, SimpleIntegerProperty> entityCounts, SimpleIntegerProperty totalCount) {
        this.setOpacity(0.5);
        this.setPrefWidth(getAppWidth());

        // Button menu
        HBox menuContainerButtons = getMenuContainerButtons();
        this.setLeft(menuContainerButtons);

        // Entity count bars
        VBox progressBarsContainer = new VBox();
        progressBarsContainer.setAlignment(Pos.CENTER_RIGHT);
        progressBarsContainer.setPadding(new Insets(10));
        Arrays.stream(RPSApp.EntityType.values()).forEach((type) -> {
            Label progressBarLabel = new Label(type.name());
            ProgressBar progressBar = new javafx.scene.control.ProgressBar();
            progressBar.setPrefWidth(200.0);
            progressBar.progressProperty().bind(entityCounts.get(type).divide(totalCount.add(0.1))); // Prevent division by zero
            HBox hBox = new HBox(progressBarLabel, progressBar);
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            progressBarsContainer.getChildren().add(hBox);
        });
        Label totalCountLabel = new Label();
        totalCountLabel.textProperty().bind(totalCount.asString());
        progressBarsContainer.getChildren().add(totalCountLabel);
        this.setRight(progressBarsContainer);
    }

    private HBox getMenuContainerButtons() {
        Button clearButton = ButtonFactory.createButton(Feather.X_CIRCLE, e -> {
            getGameWorld().removeEntities(getGameWorld().getEntitiesCopy());
            // getInput().clearAll();
        });

        ToggleButton killModeButton = ButtonFactory.createToggleButton(MaterialDesignS.SKULL_CROSSBONES_OUTLINE,
                e -> RPSApp.setKillMode(!RPSApp.isKillMode()));

        ToggleButton playPauseButton = ButtonFactory.createToggleButton(Feather.PAUSE_CIRCLE, e -> {
            if (RPSApp.isPauseMode()) {
                getGameWorld().getEntities().stream().filter(ent -> !ent.isType(RPSApp.SpecialEntityType.ZAP_ZONE)).forEach(entity -> {
                    entity.removeComponent(FloatMoveComponent.class);
                    entity.addComponent(new MoveComponent());
                });
            }
            else {
                getGameWorld().getEntities().stream().filter(ent -> !ent.isType(RPSApp.SpecialEntityType.ZAP_ZONE)).forEach(entity -> {
                    entity.removeComponent(MoveComponent.class);
                    entity.addComponent(new FloatMoveComponent());
                });
            }
            RPSApp.setPauseMode(!RPSApp.isPauseMode());
        });

        HBox menuContainerButtons = new HBox(clearButton, killModeButton, playPauseButton, zapZoneButton);
        // menuContainerButtons.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        menuContainerButtons.setSpacing(10);
        menuContainerButtons.setTranslateX(10);
        menuContainerButtons.setCursor(Cursor.DEFAULT);
        menuContainerButtons.setMaxHeight(0);
        menuContainerButtons.setOnMouseClicked(Event::consume);
        return menuContainerButtons;
    }

    public ZapZoneButton getZapZoneButton() {
        return zapZoneButton;
    }
}
