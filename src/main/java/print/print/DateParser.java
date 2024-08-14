package print.print;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {

    static String[] patternsArray2 = {
            "M/d/yyyy h:mm:ss tt",
            "M/d/yyyy h:mm:ss a",
            "M/d/yyyy h:mm tt",
            "MM/dd/yyyy HH:mm:ss",
            "M/d/yyyy",
            "MMMM dd",
            "dddd, dd MMMM yyyy",
            "ddd, dd MMM yyyy HH:mm:ss GMT",
            "yyyy'-'MM'-'dd'T'HH':'mm':'ss.fffffffK",
            "ddd MMM d HH:mm:ss yyyy",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            "yyyyMMdd'T'HHmmssSSS",
            "yyyyMMdd'T'HHmmss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd'T'HH:mm:ss",
            "yyyy/MM/dd",
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy'T'HH:mm:ss",
            "dd/MM/yyyy",
            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy'T'HH:mm:ss",
            "MM/dd/yyyy",
            "MMM dd, yyyy HH:mm:ss",
            "MMM dd, yyyy'T'HH:mm:ss",
            "MMM dd, yyyy",
            "dd-MMM-yyyy HH:mm:ss",
            "dd-MMM-yyyy'T'HH:mm:ss",
            "dd-MMM-yyyy",
            "dd MMM yyyy HH:mm:ss",
            "dd MMM yyyy'T'HH:mm:ss",
            "dd MMM yyyy",
            "dd.MM.yyyy HH:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy HH:mm:s", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy H:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy H:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy HH:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy HH:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy HH:mm:ss", // Pattern for "18.07.2024 02:07:38"
            "dd.MM.yyyy H:mm:ss",
            "dd-MM-yyyy H:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
    };


    private static final List<String> patternsList2 = Arrays.asList(patternsArray2);

    public static LocalDateTime parseDateTime2(String dateString) {
        for (String pattern : patternsList2) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDateTime.parse(dateString, formatter);
            } catch (IllegalArgumentException | DateTimeParseException e) {
                // Log the exception or handle it as needed
            }
        }
        System.err.println("Ошибка при разборе даты и времени: Не удалось разобрать дату " + dateString);
        return LocalDateTime.now(ZoneId.of("Asia/Bishkek"));
    }
    public static LocalDateTime parseDateTime(String dateString) {
        // Получение локали системы
        Locale locale = Locale.getDefault();
        // Получение формата даты и времени для текущей локали
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", locale);

        try {
            // Парсинг даты и времени
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
           return parseDateTime2(dateString);
        }
    }

}
