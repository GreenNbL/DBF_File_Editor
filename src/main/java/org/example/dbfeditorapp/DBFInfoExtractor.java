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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public static void saveFilteredDBFFile(String dbfFilePath, String newDbfFilePath) {
        ArrayList<Integer> columnIndices= (ArrayList<Integer>) EditorWindow.getSelectedRows();
        //ArrayList<String> columnNames =getInfoColumn(dbfFilePath);
        try {
            DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath));
            File newDbfFile = new File(newDbfFilePath);
            DBFWriter writer = new DBFWriter(newDbfFile);
            Object[] row;
            ArrayList<DBFField> newFileds=new ArrayList<DBFField>();
            // Устанавливаем типы полей и их названия в новом DBF
            for (int index : columnIndices) {
                DBFField field = reader.getField(index);
                DBFField newField = new DBFField();
                newField.setName(field.getName()); // Имя столбца
                newField.setDataType(field.getDataType()); // Тип поля
                if(field.getDataType()!=68)
                    newField.setFieldLength(field.getFieldLength()); // Длина поля
                if(field.getDataType()==78)
                    newField.setDecimalCount(field.getDecimalCount()); // Количество десятичных знаков для чисел
                newFileds.add(newField);

            } writer.setFields(newFileds.toArray(new DBFField[0])); // Добавляем поле в новый файл

            // Чтение всех записей в файле
            while ((row = reader.nextRecord()) != null) {

                Object[] filteredRow = new Object[columnIndices.size()];

                for (int i = 0; i < columnIndices.size(); i++) {
                    int columnIndex = columnIndices.get(i);
                    Object value = row[columnIndex];
                    System.out.println(value);

                    // Обработка даты
                    if (value instanceof Date) {
                        // System.out.println(value);
                        filteredRow[i] = convertDateString(value.toString()); // Записываем как Date
                    } else {
                        filteredRow[i] = value; // Сохраняем другие типы значений

                    }
                }

                writer.addRecord(filteredRow); // Записываем отфильтрованную запись
            }
            System.out.println("Writing finished");
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    // Метод для преобразования даты из формата "dd.MM.yy" в "dd.MM.yyyy"
    private static Date convertDateString(String dateString) throws ParseException {
        // Извлечение года из строки
        String[] parts = dateString.split(" "); // Разделяем строку по пробелам
        String yearPart = parts[parts.length - 1]; // Берем последний элемент (год)
        int year;

        if (yearPart.length() <= 2) { // Если год двухзначный или одинарный
            year = Integer.parseInt(yearPart); // Конвертируем в int
            // Добавляем "19" или "20" в зависимости от значения года
            year += (year < 50) ? 2000 : 1900; // Добавляем "20" или "19"

            // Добавим 2 дня к текущей дате
            dateString = dateString.substring(0, dateString.length() - yearPart.length()) + year;

            // Изменяем дату перед парсингом, чтобы увеличить дни на 2
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date tempDate = inputFormat.parse(dateString); // Парсим с использованием полного года

            // Увеличиваем дни на 2
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempDate);
            calendar.add(Calendar.DAY_OF_MONTH, 2); // Добавляем 2 дня

            return calendar.getTime(); // Возвращаем новый объект Date
        } else if (yearPart.length() == 4) { // Если год четырехзначный
            year = Integer.parseInt(yearPart); // Конвертируем в int
            dateString = dateString.substring(0, dateString.length() - yearPart.length()) + year;
            // Парсим и просто возвращаем дату
        } else {
            throw new IllegalArgumentException("Некорректный формат года: " + yearPart);
        }

        // Форматируем строку для парсинга
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = inputFormat.parse(dateString); // Парсим с помощью полного года

        return date; // Возвращаем объект Date
    }



}
