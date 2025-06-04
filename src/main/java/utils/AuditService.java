package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final String CSV_PATH = "src/main/data/log.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void logSystemAction(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = actionName + "," + timestamp + "\n";

        try (FileWriter writer = new FileWriter(CSV_PATH, true)) {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException("Could not append to the log.csv file", e);
        }
    }
}
