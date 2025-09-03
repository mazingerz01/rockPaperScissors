package org.maz.rps.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import org.jetbrains.annotations.NotNull;

public class RPSSceneFactory extends SceneFactory {

    @NotNull
    @Override
    // GameMenu for in game menu, MainMenu for menu on startup
    public FXGLMenu newGameMenu() {
        return new RPSMenu();
    }
}
