package com.example.inventoryapiserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final String[] HEADERS = {"№ п/п", "Основное средство", "Код", "Инвентарный номер", "Дата выпуска",
            "Заводской номер", "Корпус", "Местоположение", "Количество", "Правильно расположен"};

    public static long convertToMilliseconds(String dateStr) {
        long milliseconds = 0;

        if (!dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date date = dateFormat.parse(dateStr);

                milliseconds = date.getTime();
            } catch (ParseException e) {
                System.err.println("Ошибка при преобразовании даты: " + e.getMessage());
            }
        }

        return milliseconds;
    }

    public static String convertToDate(Long milliseconds) {
        if (milliseconds == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(new Date(milliseconds));
    }
}
