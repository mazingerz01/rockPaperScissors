package org.maz;

import com.almasb.fxgl.entity.Entity;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.maz.components.FloatMoveComponent;
import org.maz.components.MoveComponent;
import org.maz.zapzone.ZapZoneButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class InGameUI extends BorderPane {
    InGameUI(EnumMap<Main.EntityType, SimpleIntegerProperty> entityCounts, SimpleIntegerProperty totalCount) {
        this.setOpacity(0.5);
        this.setPrefWidth(getAppWidth());

        // Button menu
        HBox menuContainerButtons = getMenuContainerButtons();
        this.setLeft(menuContainerButtons);

        // Entity count bars
        VBox progressBarsContainer = new VBox();
        progressBarsContainer.setAlignment(Pos.CENTER_RIGHT);
        progressBarsContainer.setPadding(new Insets(10));
        Arrays.stream(Main.EntityType.values()).forEach((type) -> {
            Label progressBarLabel = new Label(type.name());
            ProgressBar progressBar = new javafx.scene.control.ProgressBar();
            progressBar.setPrefWidth(200.0);
            progressBar.progressProperty().bind(entityCounts.get(type).divide(totalCount.add(0.1)));
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
            List<Entity> entities = new ArrayList<>(getGameWorld().getEntities());
            getGameWorld().removeEntities(entities);
        });

        ToggleButton killModeButton = ButtonFactory.createToggleButton(MaterialDesignS.SKULL_CROSSBONES_OUTLINE,
                e -> Main.setKillMode(!Main.isKillMode()));

        ToggleButton playPauseButton = ButtonFactory.createToggleButton(Feather.PAUSE_CIRCLE, e -> {
            if (Main.isPauseMode()) {
                getGameWorld().getEntities().stream().filter(ent -> !ent.isType(Main.SpecialEntityType.ZAP_ZONE)).forEach(entity -> {
                    entity.removeComponent(FloatMoveComponent.class);
                    entity.addComponent(new MoveComponent());
                });
            } else {
                getGameWorld().getEntities().stream().filter(ent -> !ent.isType(Main.SpecialEntityType.ZAP_ZONE)).forEach(entity -> {
                    entity.removeComponent(MoveComponent.class);
                    entity.addComponent(new FloatMoveComponent());
                });
            }
            Main.setPauseMode(!Main.isPauseMode());
        });

        HBox menuContainerButtons = new HBox(clearButton, killModeButton, playPauseButton, new ZapZoneButton());
        // menuContainerButtons.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        menuContainerButtons.setSpacing(10);
        menuContainerButtons.setTranslateX(10);
        menuContainerButtons.setCursor(Cursor.DEFAULT);
        menuContainerButtons.setMaxHeight(0);
        return menuContainerButtons;
    }

}
