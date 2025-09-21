package at.fhtw.Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Logger {
    private static final String logPath = System.getProperty("user.dir") + "/LogFiles";

    public static void log(LogType logType, String message) {
        String logFilePath = prepareLogging();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFilePath, StandardCharsets.UTF_8, true))) {
            if(LogType.INFO == logType) {
                bw.write(LocalDateTime.now().toLocalTime() + " INFO: " + message);
            } else if (LogType.ERROR == logType) {
                bw.write(LocalDateTime.now().toLocalTime() + " ERROR: " + message);
            }

            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String prepareLogging() {
        String logFileName = logPath + "/MRP_" + Instant.now().atZone(ZoneId.systemDefault()).toLocalDate() + ".log";

        try {
            if(!Files.exists(Paths.get(logPath))) {
                Files.createDirectories(Path.of(logPath));
            }

            if(!Files.exists(Paths.get(logFileName))) {
                Files.createFile(Paths.get(logFileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logFileName;
    }
}
