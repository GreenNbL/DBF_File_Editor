package org.example.dbfeditorapp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringToDateExample {
    public static void main(String[] args) {
        String dateString = "Sat Jan 01 00:00:00 MSK 91"; // Ваша строка даты

        try {
            // Заменяем "MSK" на временной зону GMT+3
            dateString = dateString.replace("MSK", "GMT+3");

            // Определяем формат входной даты
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yy");
            // Указываем, что мы используем время по умолчанию
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));

            // Парсим строку в объект Date
            Date date = inputFormat.parse(dateString);

            // Определяем формат выходной даты
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
            // Устанавливаем нужный часовой пояс вывода
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));

            // Форматируем и выводим результат
            String formattedDate = outputFormat.format(date);
            System.out.println("Преобразованная дата: " + formattedDate); // Вывод: 01.01.1991
        } catch (ParseException e) {
            e.printStackTrace(); // Обработка исключения, если не удается распарсить
        }
    }
}
