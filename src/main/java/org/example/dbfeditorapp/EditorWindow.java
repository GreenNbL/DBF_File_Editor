package org.example.dbfeditorapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.dbfeditorapp.DBFInfoExtractor.saveFilteredDBFFile;

public class EditorWindow {
    private static TableView<Item> table;
    private String fileAdress;
    private  Stage owner;
    private  Stage newStage;
    private  Label  infoSaving =new Label();
    public EditorWindow(Stage owner, String fileAdress)
    {
        this.fileAdress = fileAdress;
        this.owner = owner;
    }
    public void openNewWindow() {
        newStage = new Stage(); // Создание нового окна
        // Создание нового окна
        newStage.setTitle("Окно редактирования");

        // Устанавливаем модальность, чтобы заблокировать главное окно
        newStage.initModality(Modality.APPLICATION_MODAL);
        // Центрируем новое окно относительно основного окна
        newStage.setX(owner.getX() );
        newStage.setY(owner.getY() );

        newStage.initOwner(owner);
        // Создание кнопок
        Button saveAsButton = new Button("Сохранить как");
        saveAsButton.setOnAction(e -> saveSelectedItems()); // Вызов метода сохранения, запущенного в UI-потоке


        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> newStage.close());

        HBox buttonLayout = new HBox(10, infoSaving, saveAsButton, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center-right;");

        table=createTable(fileAdress);

        VBox layout = new VBox(table, buttonLayout);

        newStage.setScene(new Scene(layout, 400, 300));


        // Показать новое окно
        newStage.showAndWait();

    }
    public void saveSelectedItems() {
        infoSaving.setText("Файл сохраняется...");
        // Создаем DirectoryChooser для выбора директории
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить DBF файл");
        // Открыть диалог выбора файла и установить имя файла по умолчанию
        File defaultFile = new File(fileAdress);
        fileChooser.setInitialFileName(getFileNameWithoutExtension(defaultFile.getName())+"(2)");
        fileChooser.setInitialDirectory(defaultFile.getParentFile());
        File fileToSave = fileChooser.showSaveDialog(null);
        // Проверяем, был ли выбран файл
        if (fileToSave != null) {
            // Добавляем расширение .dbf
            fileToSave = new File(fileToSave.getAbsolutePath() + ".dbf");

            // Проверка на существование файла
            if (fileToSave.exists()) {
                // Запрос подтверждения у пользователя
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setX(newStage.getX());
                alert.setY(newStage.getY());
                alert.setTitle("Подтверждение");
                alert.setHeaderText("Файл с таким именем уже существует.");
                alert.setContentText("Хотите перезаписать существующий файл?");

                ButtonType yesButton = new ButtonType("Да");
                ButtonType noButton = new ButtonType("Нет");
                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == noButton) {
                    infoSaving.setText("");
                    return; // Если пользователь выбрал "Нет", завершаем метод
                }
            }

            // Вызов функции для сохранения DBF файла
            saveFilteredDBFFile(fileAdress, fileToSave.getAbsolutePath());
            infoSaving.setText("Файл сохранен");
        }
        /*

        if (fileToSave != null) {
            fileToSave = new File(fileToSave.getAbsolutePath()+ ".dbf");
            // Вызов вашей функции для сохранения DBF файла
            saveFilteredDBFFile(fileAdress, fileToSave.getAbsolutePath());
        }*/
    }
    private static String getFileNameWithoutExtension(String fileName) {
        // Находим индекс последней точки
        int index = fileName.lastIndexOf('.');
        // Если точка найдена, возвращаем подстроку до точки; иначе возвращаем полное имя файла
        if (index > 0) {
            return fileName.substring(0, index);
        }
        return fileName; // Если нет расширения, возвращаем полное имя
    }
    public static List<Integer> getSelectedRows() {
        List<Integer> selectedIndices = new ArrayList<>();
        // Перебираем все строки таблицы
        for (int i = 0; i < table.getItems().size(); i++) {
            Item item = table.getItems().get(i);
            if (item.isSelected()) {
                selectedIndices.add(i); // Добавляем индекс строки, если чекбокс выбран
            }
        }
        System.out.println("Выбранные номера строк: " + selectedIndices);
        return selectedIndices; // Возвращаем список индексов
    }
    private  ObservableList<Item> generateData(int count,String fileAdress) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<String> columnNames=DBFInfoExtractor.getInfoColumn(fileAdress);
        for (int i = 0; i < count; i++) {
            items.add(new Item( columnNames.get(i)));
        }
        return items;
    }
    private  TableView<Item> createTable(String fileAdress)
    {
        int numberOfItems = DBFInfoExtractor.getAmountOfColumns(fileAdress); // Пример количества элементов
        ObservableList<Item> data = generateData(numberOfItems,fileAdress);

        TableView<Item> tableView = new TableView<>(data);

        // Столбец с номерами по порядку
        TableColumn<Item, Integer> indexColumn = new TableColumn<>("№");
        indexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Item, Integer> features) {
                int index = tableView.getItems().indexOf(features.getValue()) + 1;
                return new SimpleIntegerProperty(index).asObject();
            }
        });
        // Столбец чекбоксов
        TableColumn<Item, Boolean> selectColumn = new TableColumn<>("Выбрать");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        // Столбец с именем позиции
        TableColumn<Item, String> nameColumn = new TableColumn<>("Имя позиции");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().addAll(indexColumn, selectColumn, nameColumn);
        tableView.setEditable(true); // Делаем таблицу редактируемой

        return tableView;
    }


}
