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

    // Export games package so FXGl can access them
    exports org.maz.rps;
    exports org.maz.rps.specialentities;
}
