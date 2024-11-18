package org.example.dbfeditorapp;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;

public class DBFInfoExtractor {

    public static int getAmountOfColumns(String dbfFilePath)
    {
        int numberOfFields=0;
        try
        {
            DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath));
            numberOfFields = reader.getFieldCount(); // Получаем количество поле
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (DBFException e) {
            throw new RuntimeException(e);
        }
        finally {
            return numberOfFields;
        }
    }

    public static ArrayList<String> getInfoColumn(String dbfFilePath) {
        // Укажите путь к вашему DBF файлу
        //String dbfFilePath = "C024302.dbf";
        ArrayList<String> columnNames = new ArrayList<String>();
        char[] typeMapping = new char[256]; // Предположим, что кодов не более 255
        typeMapping[67] = 'C'; // Character
        typeMapping[68] = 'D'; // Date
        typeMapping[78] = 'N'; // Numeric
        // Обработка чтения DBF файла
        try
        {
            DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath));
            int numberOfFields = reader.getFieldCount(); // Получаем количество полей

            // Перебираем и выводим информацию о каждом поле
            for (int i = 0; i < numberOfFields; i++) {
                DBFField field = reader.getField(i); // Получаем поле
                String fieldName = field.getName(); // Имя поля
                int fieldTypeCode = field.getDataType();
                char fieldType = typeMapping[fieldTypeCode];
                int fieldLength = field.getFieldLength(); // Длина поля
                // Вывод информации в требуемом формате
                if (fieldType == 'N') {
                    int fieldDecimals = field.getDecimalCount(); // Количество знаков после запятой
                    // Форматируем вывод для числовых полей
                    columnNames.add(fieldName + "," + fieldType + "," + fieldLength + "," + fieldDecimals);
                    //System.out.println(fieldName + "," + fieldType + "," + fieldLength + "," + fieldDecimals);
                } else {
                    columnNames.add(fieldName + "," + fieldType + "," + fieldLength);
                    // Для остальных типов
                    //System.out.println(fieldName + "," + fieldType + "," + fieldLength);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода
        }
        finally {
            return columnNames;
        }
    }
    public static void saveFilteredFile(String dbfFilePath, String newDbfFilePath) {
        List columnIndices= (List)EditorWindow.getSelectedRows();

    }
}
