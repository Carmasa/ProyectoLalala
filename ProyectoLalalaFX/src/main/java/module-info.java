module org.example.proyectolalalafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.example.proyectolalalafx to javafx.fxml;
    exports org.example.proyectolalalafx;
}