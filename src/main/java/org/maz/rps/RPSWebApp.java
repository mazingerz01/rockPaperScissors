package org.maz.rps;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.almasb.fxgl.app.FXGLPane;
import com.almasb.fxgl.app.GameApplication;

public class RPSWebApp extends Application {

    @Override
    public void start(Stage stage) {
        System.setProperty("fxgl.isBrowser", "true");

        GameApplication app = new RPSApp();
        FXGLPane fxglRoot = GameApplication.embeddedLaunch(app);

        stage.setScene(new Scene(new StackPane(fxglRoot)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
