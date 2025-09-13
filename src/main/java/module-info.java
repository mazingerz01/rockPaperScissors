open module rockPaperScissors {
    // JavaFX
    requires javafx.controls;
    requires javafx.graphics;

    // FXGL
    requires com.almasb.fxgl.all;

    // Theme / UI
    requires atlantafx.base;

    // Icons
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.materialdesign2;

    // Misc.
    requires annotations;
    requires java.desktop;

    // Export your game package
    exports org.maz.rps;
}
