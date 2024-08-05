package print.print;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import print.print.dto.InfoForPrint;
import print.print.dto.PhotoForLocalSave;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button print;

    @FXML
    public void print() {
        if (!checkExists()) {
            saveInfoInComp();
        } else {
            String localInSystem = getLocalInSystem();
            List<PhotoForLocalSave> onlyToday = getOnlyToday(getAllPhotos(localInSystem));
            InfoForPrint infoForPrint = getInfoForPrint(onlyToday);
            PrintInfoForPrint printer = new PrintInfoForPrint(infoForPrint);
            printer.print();

        }
    }

    private InfoForPrint getInfoForPrint(List<PhotoForLocalSave> photos) {
        if (photos == null || photos.isEmpty()) {
            throw new IllegalArgumentException("Photos list cannot be null or empty");
        }

        InfoForPrint info = new InfoForPrint();
        int successA4 = 0;
        int successA5 = 0;
        int successA6 = 0;
        int successUnknown = 0;
        int unSuccessA4 = 0;
        int unSuccessA5 = 0;
        int unSuccessA6 = 0;
        int unSuccessUnknown = 0;
        photos.sort(comparator);
        LocalDateTime itsFirst = photos.getFirst().getCreatedAtOrig();
        LocalDateTime itsLast = photos.getLast().getCreatedAtOrig();

        for (PhotoForLocalSave photo : photos) {
            if (photo.isStatus()) {
                if (photo.getSize().contains("A4")) {
                    successA4 += photo.getAmount();
                } else if (photo.getSize().contains("A5")) {
                    successA5 += photo.getAmount();
                } else if (photo.getSize().contains("A6")) {
                    successA6 += photo.getAmount();
                } else {
                    successUnknown += photo.getAmount();
                }
            } else {
                if (photo.getSize().contains("A4")) {
                    unSuccessA4 += photo.getAmount();
                } else if (photo.getSize().contains("A5")) {
                    unSuccessA5 += photo.getAmount();
                } else if (photo.getSize().contains("A6")) {
                    unSuccessA6 += photo.getAmount();
                } else {
                    unSuccessUnknown += photo.getAmount();
                }
            }
        }
        info.setCurrentTime(LocalDateTime.now(ZoneId.of("Asia/Bishkek")).toString());
        info.setFirstTimePrint(itsFirst.toString());
        info.setLastTimePrint(itsLast.toString());
        info.setSuccessA4(String.valueOf(successA4));
        info.setSuccessA5(String.valueOf(successA5));
        info.setSuccessA6(String.valueOf(successA6));
        info.setUnknown(String.valueOf(successUnknown));
        info.setUnSuccessA4(String.valueOf(unSuccessA4));
        info.setUnSuccessA5(String.valueOf(unSuccessA5));
        info.setUnSuccessA6(String.valueOf(unSuccessA6));
        info.setUnUnknown(String.valueOf(unSuccessUnknown));
        info.setEmail(getEmail());
        return info;
    }


    Comparator<PhotoForLocalSave> comparator = new Comparator<>() {

        @Override
        public int compare(PhotoForLocalSave o1, PhotoForLocalSave o2) {
            return o1.getCreatedAtOrig().compareTo(o2.getCreatedAtOrig());
        }
    };

    private List<PhotoForLocalSave> getOnlyToday(List<PhotoForLocalSave> photos) {
        List<PhotoForLocalSave> allPhotos = new ArrayList<>();
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Bishkek"));

        for (PhotoForLocalSave photo : photos) {
            if (photo.getCreatedAtOrig().toLocalDate().equals(today)) {
                allPhotos.add(photo);
            }
        }
        return allPhotos;
    }

    private List<PhotoForLocalSave> getAllPhotos(String absolutePathForFolder) {
        List<PhotoForLocalSave> photos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(absolutePathForFolder), "*.json")) {
            for (Path entry : stream) {
                try {
                    JsonNode node = objectMapper.readTree(entry.toFile());
                    if (node.isArray()) {
                        List<PhotoForLocalSave> photoList = objectMapper.readValue(node.traverse(), new TypeReference<List<PhotoForLocalSave>>() {
                        });
                        photos.addAll(photoList);
                    } else {
                        PhotoForLocalSave photo = objectMapper.readValue(node.traverse(), PhotoForLocalSave.class);
                        photos.add(photo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photos;
    }


    private void saveInfoInComp() {
        String fileName = "values.properties";
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + fileName;
        Properties properties = new Properties();
        properties.setProperty("local", "change Me 2 clesh'");
        properties.setProperty("formatter", "'yyyy-MM-dd HH:mm:ss'");
        properties.setProperty("email", "change Me");

        try (OutputStream output = new FileOutputStream(filePath);
             OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            properties.store(writer, "Key-Value Pairs");
            System.out.println("Запись успешно сохранена в: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error for save info in comp: " + e.getMessage());
        }
    }

    private boolean checkExists() {
        String fileName = "values.properties";
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + fileName;
        File file = new File(filePath);
        return file.exists();
    }

    private static String getLocalInSystem() {
        String fileName = "values.properties";
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + fileName;
        Properties properties = new Properties();
        String value = "";

        try {
            FileInputStream input = new FileInputStream(filePath);

            String var6;
            try {
                properties.load(input);
                value = properties.getProperty("local");
                System.out.println("Значение local: " + value);
                var6 = value;
            } catch (Throwable var9) {
                try {
                    input.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            input.close();
            return var6;
        } catch (IOException var10) {
            IOException io = var10;
            io.printStackTrace();
            return value;
        }

    }
    private static String getEmail() {
        String fileName = "values.properties";
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + fileName;
        Properties properties = new Properties();
        String value = "";

        try {
            FileInputStream input = new FileInputStream(filePath);

            String var6;
            try {
                properties.load(input);
                value = properties.getProperty("email");
                System.out.println("Значение email: " + value);
                var6 = value;
            } catch (Throwable var9) {
                try {
                    input.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            input.close();
            return var6;
        } catch (IOException var10) {
            IOException io = var10;
            io.printStackTrace();
            return value;
        }

    }

    public static LocalDateTime parseDateTime(String dateString) {
        String formatterString = getFormatter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterString);

        try {
            // Парсинг даты и времени
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Ошибка при разборе даты и времени: Не удалось разобрать дату " + dateString + " " + e.getMessage());
        }
        return LocalDateTime.now();
    }

    private static String getFormatter() {
        String fileName = "values.properties";
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + fileName;
        Properties properties = new Properties();
        String value = "";

        try {
            FileInputStream input = new FileInputStream(filePath);

            String var6;
            try {
                properties.load(input);
                value = properties.getProperty("formatter");
                System.out.println("Значение formatter: " + value);
                var6 = value;
            } catch (Throwable var9) {
                try {
                    input.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            input.close();
            return var6;
        } catch (IOException var10) {
            IOException io = var10;
            io.printStackTrace();
            return value;
        }

    }
}
