package org.example.dbfeditorapp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditorWindow {
    public static void openNewWindow(Stage owner, String fileAdress) {
        // Создание нового окна
        Stage newStage = new Stage();
        newStage.setTitle("Новое окно");

        // Устанавливаем модальность, чтобы заблокировать главное окно
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(owner);
        // Создание кнопок
        Button saveAsButton = new Button("Сохранить как");
        //saveAsButton.setOnAction(e -> saveSelectedItems(tableView));
        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> newStage.close());

        HBox buttonLayout = new HBox(10, saveAsButton, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center-right;");

        TableView<Item> table=createTable(fileAdress);

        VBox layout = new VBox(table, buttonLayout);

        newStage.setScene(new Scene(layout, 400, 300));
        // Показать новое окно
        newStage.showAndWait();

    }
    private static ObservableList<Item> generateData(int count,String fileAdress) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<String> columnNames=DBFInfoExtractor.getInfoColumn(fileAdress);
        for (int i = 0; i < count; i++) {
            items.add(new Item( columnNames.get(i)));
        }
        return items;
    }
    private static TableView<Item> createTable(String fileAdress)
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
