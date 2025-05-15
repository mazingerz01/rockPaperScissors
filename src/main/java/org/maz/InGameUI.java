package org.maz;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class InGameUI extends BorderPane {

    private final ToggleButton killModeButton = new ToggleButton("kill mode");
    private final Button playPauseButton = new Button();
    private StackPane powerUpContainer;

    InGameUI(EnumMap<Main.EntityType, SimpleIntegerProperty> entityCounts, SimpleIntegerProperty totalCount) {
        this.setOpacity(0.5);
        this.setPrefWidth(getAppWidth());

        // Button menu
        HBox menuContainerButtons = getMenuContainerButtons();
        this.setLeft(menuContainerButtons);


        // Entity count bars
        VBox progressBars = new VBox();
        progressBars.setAlignment(Pos.CENTER_RIGHT);
        Arrays.stream(Main.EntityType.values()).forEach((type) -> {
            Label progressBarLabel = new Label(type.name());
            ProgressBar progressBar = new ProgressBar();
            progressBar.setId(type.name());
            progressBar.setWidth(100);
            progressBar.setMinValue(-1);
            progressBar.maxValueProperty().bind(totalCount);
            progressBar.currentValueProperty().bind(entityCounts.get(type));
            HBox hBox = new HBox(progressBarLabel, progressBar);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            progressBars.getChildren().add(hBox);
        });
        Label totalCountLabel = new Label();
        totalCountLabel.textProperty().bind(totalCount.asString());
        progressBars.getChildren().add(new HBox(totalCountLabel));
        this.setRight(progressBars);
    }

    private HBox getMenuContainerButtons() {
        Button clearButton = new Button("clear");
        clearButton.setCursor(Cursor.DEFAULT);
        clearButton.setOnAction(e -> {
            System.out.println("clear");
            List<Entity> entities = new ArrayList<>(getGameWorld().getEntities());
            getGameWorld().removeEntities(entities);
        });

        killModeButton.setCursor(Cursor.DEFAULT);

        playPauseButton.setCursor(Cursor.DEFAULT);
        playPauseButton.setGraphic(new ImageView(FXGL.getAssetLoader().loadImage("pause.png")));

        getPowerUpContainer();

        HBox menuContainerButtons = new HBox(clearButton, killModeButton, playPauseButton, powerUpContainer);
        menuContainerButtons.setSpacing(10);
        menuContainerButtons.setTranslateX(10);
        return menuContainerButtons;
    }

    private void getPowerUpContainer() {
        powerUpContainer = new StackPane();
        powerUpContainer.setCursor(Cursor.DEFAULT);
        powerUpContainer.addEventFilter(MouseEvent.ANY, event -> {
            event.consume();
            System.out.println("Mouse event consumed: " + event.getEventType());
        });


        powerUpContainer.setAlignment(Pos.CENTER);
        final Image powerUp = FXGL.getAssetLoader().loadImage("powerup.png");
        powerUpContainer.setOnDragDetected((event -> {
            getGameScene().setCursor(new ImageCursor(powerUp));
        }));
        final ImageView powerUpImageView = new ImageView(powerUp);
        powerUpContainer.getChildren().add(powerUpImageView);

        // Hide powerup behind a rectangle
        Rectangle rect = new Rectangle(powerUpImageView.getImage().getWidth(), powerUpImageView.getImage().getHeight());
        rect.setFill(Color.WHITE);
        powerUpContainer.getChildren().add(rect);
        powerUpImageView.setOpacity(0.25);
        applyAnimations(rect, powerUpImageView);
    }

    private void applyAnimations(Rectangle rect, ImageView powerUpImageView) {
        // Slowly make powerup visible
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(40), rect);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(0);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1);
        scaleTransition.setOnFinished((event) -> {
            powerUpImageView.setOpacity(1.0);
            // Pulse animation
            ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(0.45), powerUpImageView);
            pulseAnimation.setByX(1.4);
            pulseAnimation.setByY(1.4);
            pulseAnimation.setAutoReverse(true);
            pulseAnimation.setCycleCount(2);
            pulseAnimation.play();
        });
        scaleTransition.play();
    }

    public ToggleButton getKillModeButton() {
        return killModeButton;
    }

    public Button getPlayPauseButton() {
        return playPauseButton;
    }

}
