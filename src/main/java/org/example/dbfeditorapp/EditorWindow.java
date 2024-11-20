package org.example.dbfeditorapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.example.dbfeditorapp.DBFInfoExtractor.saveFilteredDBFFile;

public class EditorWindow {
    private static TableView<Item> table;
    private String fileAdress;
    private  Stage owner;
    public EditorWindow(Stage owner, String fileAdress)
    {
        this.fileAdress = fileAdress;
        this.owner = owner;
    }
    public void openNewWindow() {
        Stage newStage = new Stage(); // Создание нового окна
        // Создание нового окна
        newStage.setTitle("Новое окно");

        // Устанавливаем модальность, чтобы заблокировать главное окно
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(owner);
        // Создание кнопок
        Button saveAsButton = new Button("Сохранить как");
        saveAsButton.setOnAction(e -> saveSelectedItems(fileAdress));
        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> newStage.close());

        HBox buttonLayout = new HBox(10, saveAsButton, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center-right;");

        table=createTable(fileAdress);

        VBox layout = new VBox(table, buttonLayout);

        newStage.setScene(new Scene(layout, 400, 300));


        // Показать новое окно
        newStage.showAndWait();

    }
    public static void saveSelectedItems(String fileAdress) {
        // Создаем DirectoryChooser для выбора директории
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить DBF файл");
        // Открыть диалог выбора файла и установить имя файла по умолчанию
        File defaultFile = new File(fileAdress);
        fileChooser.setInitialFileName(getFileNameWithoutExtension(defaultFile.getName())+"(2)");
        fileChooser.setInitialDirectory(defaultFile.getParentFile());
        // Показываем диалог для сохранения файла
        File fileToSave = fileChooser.showSaveDialog(null);
        if (fileToSave != null) {
            fileToSave = new File(fileToSave.getAbsolutePath()+ ".dbf");
            // Вызов вашей функции для сохранения DBF файла
            saveFilteredDBFFile(fileAdress, fileToSave.getAbsolutePath());
        }
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

        // Столбец чекбоксов
        TableColumn<Item, Boolean> selectColumn = new TableColumn<>("Выбрать");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        // Столбец с именем позиции
        TableColumn<Item, String> nameColumn = new TableColumn<>("Имя позиции");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().addAll(selectColumn, nameColumn);
        tableView.setEditable(true); // Делаем таблицу редактируемой

        return tableView;
    }


}
