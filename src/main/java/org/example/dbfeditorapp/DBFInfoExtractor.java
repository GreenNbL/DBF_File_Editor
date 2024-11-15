package org.example.dbfeditorapp;

import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFField;

import java.io.FileInputStream;
import java.io.IOException;

public class DBFInfoExtractor {
    public void getInfoColumn(String dbfFilePath) {
        // Укажите путь к вашему DBF файлу
        //String dbfFilePath = "C024302.dbf";

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
                    System.out.println(fieldName + "," + fieldType + "," + fieldLength + "," + fieldDecimals);
                } else {
                    // Для остальных типов
                    System.out.println(fieldName + "," + fieldType + "," + fieldLength);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода
        }
    }
}
