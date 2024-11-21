package org.example.dbfeditorapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dbfEditor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 350);
        stage.setTitle("DBF Editor");
        stage.setScene(scene);
        stage.show();

        // Получаем контроллер для установки обработки событий
        FileOpenner fileOpenner = fxmlLoader.getController();
        fileOpenner.setPrimaryStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}