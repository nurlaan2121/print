package print.print;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import print.print.dto.InfoForPrint;
import print.print.dto.PhotoForLocalSave;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HelloController {

    @FXML
    private ResourceBundle resources;


    @FXML
    private Button print;

    @FXML
    public void print() {
        if (!checkExists()) {
            saveInfoInComp();
        } else {
            String localInSystem = getLocalInSystem();
            List<PhotoForLocalSave> allPhotos = getAllPhotos(localInSystem);
            List<PhotoForLocalSave> onlyToday = getOnlyToday(allPhotos);
            System.out.println("Only today: " +onlyToday.size());
            onlyToday.forEach(System.out::println);
            String lastPrintTimeInYesterday = getLastPrintTimeInYesterday(allPhotos);
            InfoForPrint infoForPrint = getInfoForPrint(onlyToday);
            infoForPrint.setYesterdayLastTimePrint(lastPrintTimeInYesterday);
            PrintInfoForPrint printer = new PrintInfoForPrint(infoForPrint);
            printer.print();
            String key = getKey();
            if (key.equalsIgnoreCase("ok")) {
                deleteFileDayBeforeYesterday(localInSystem);
            }

        }
    }

    private InfoForPrint getInfoForPrint(List<PhotoForLocalSave> photos) {
        if (photos == null || photos.isEmpty()) {
            InfoForPrint info = new InfoForPrint();
            info.setCurrentTime(LocalDateTime.now(ZoneId.of("Asia/Bishkek")).toString());
            info.setFirstTimePrint("0");
            info.setLastTimePrint("0");
            info.setSuccessA4(String.valueOf(0));
            info.setSuccessA5(String.valueOf(0));
            info.setSuccessA6(String.valueOf(0));
            info.setUnknown(String.valueOf(0));
            info.setUnSuccessA4(String.valueOf(0));
            info.setUnSuccessA5(String.valueOf(0));
            info.setUnSuccessA6(String.valueOf(0));
            info.setUnUnknown(String.valueOf(0));
            info.setEmail(getEmail());
            return info;
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
        String date = getDate();
        if (date.equals("now")) {
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Bishkek"));
            for (PhotoForLocalSave photo : photos) {
                if (photo.getCreatedAtOrig().toLocalDate().equals(today)) {
                    allPhotos.add(photo);
                }
            }
            return allPhotos;
        }else {
            LocalDate parse = LocalDate.parse(date);
            for (PhotoForLocalSave photo : photos) {
                if (photo.getCreatedAtOrig().toLocalDate().equals(parse)) {
                    allPhotos.add(photo);
                }
            }
            return allPhotos;
        }
    }

    public String getLastPrintTimeInYesterday(List<PhotoForLocalSave> photos) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        LocalDateTime endOfYesterday = yesterday.atTime(LocalTime.MAX);

        return Objects.requireNonNull(photos.stream()
                .map(PhotoForLocalSave::getCreatedAtOrig)
                .filter(printTime -> !printTime.isBefore(startOfYesterday) && !printTime.isAfter(endOfYesterday))
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))).toString();
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
        properties.setProperty("email", "change Me");
        properties.setProperty("delete", "no");
        properties.setProperty("date", "now");

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

    private static String getDate() {
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
                value = properties.getProperty("date");
                System.out.println("Значение date: " + value);
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

    private static String getKey() {
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
                value = properties.getProperty("delete");
                System.out.println("Значение delete: " + value);
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


    public void deleteFileDayBeforeYesterday(String absolutePathForFolder) {
        LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
        LocalDateTime startOfDayBeforeYesterday = dayBeforeYesterday.atStartOfDay();

        File folder = new File(absolutePathForFolder);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                for (File file : files) {
                    if (file.getName().endsWith(".json")) {
                        try {
                            JsonNode node = objectMapper.readTree(file);
                            PhotoForLocalSave photo = objectMapper.treeToValue(node, PhotoForLocalSave.class);
                            LocalDateTime createdAtOrig = photo.getCreatedAtOrig(); // Assuming this method returns LocalDateTime
                            if (createdAtOrig != null && createdAtOrig.isBefore(startOfDayBeforeYesterday)) {
                                File fileForDelete = new File(photo.getImagePath());
                                deleteFile(fileForDelete);
                                deleteFile(file);
                            }
                        } catch (IOException e) {
                            System.err.println("Error processing JSON file: " + file.getName() + " - " + e.getMessage());
                        }
                    } else {
                        deleteFile(file);
                    }
                }
            }
        }
    }

    private void deleteFile(File file) {
        if (file.delete()) {
            System.out.println("Deleted file: " + file.getName());
        } else {
            System.out.println("Failed to delete file: " + file.getName());
        }
    }
}
