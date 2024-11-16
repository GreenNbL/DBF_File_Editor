module org.example.dbfeditorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javadbf;

    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires java.desktop;
    requires javafx.swing;

    opens org.example.dbfeditorapp to javafx.fxml;
    exports org.example.dbfeditorapp;
}