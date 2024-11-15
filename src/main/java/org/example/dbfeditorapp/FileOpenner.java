package org.example.dbfeditorapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import java.io.File;

public class FileOpenner {
    @FXML
    private Button chooseFileButton;

    @FXML
    private StackPane dropArea;

    private Stage primaryStage;

    private String filePath;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Обработчик события для кнопки выбора файла
        chooseFileButton.setOnAction(event -> openFileChooser());
        dropArea.setOnDragOver(event -> reactFileArea(event ));
        dropArea.setOnDragDropped(event -> openFileArea(event));
    }
    private void openFileArea(DragEvent event) {
        // Получение драга
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            // Получаем первый файл из списка
            File file = db.getFiles().get(0);
            String filePath = file.getAbsolutePath(); // Получаем путь к файлу
            DBFInfoExtractor dbfInfoExtractor = new DBFInfoExtractor();
            dbfInfoExtractor.getInfoColumn(filePath);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }
    private void reactFileArea( DragEvent event) {
        if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }
    private void openFileChooser() {
        System.out.println("Opening file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать файл");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.gif")
        );
        // Открытие диалога выбора файла
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath(); // Получаем абсолютный путь
            DBFInfoExtractor dbfInfoExtractor = new DBFInfoExtractor();
            dbfInfoExtractor.getInfoColumn(filePath);
        }
    }
}
