module org.example.proyectolalalafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.media;
    requires jlayer;

    opens org.example.proyectolalalafx to javafx.fxml;
    opens org.example.proyectolalalafx.marte to javafx.fxml;
    exports org.example.proyectolalalafx;
    exports org.example.proyectolalalafx.marte;

    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.graphics;

    opens app to javafx.fxml;
    opens controller to javafx.fxml;
    exports app;
    exports controller;
    exports player;
}