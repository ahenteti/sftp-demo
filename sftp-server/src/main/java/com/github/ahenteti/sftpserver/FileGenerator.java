package com.github.ahenteti.sftpserver;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileGenerator {

    public static final int PERIOD = 5;
    @Autowired
    private FileGeneratorConfig config;

    @Scheduled(cron = "0 * * ? * *")
    public void generate() {
        try {
            LocalDateTime now = LocalDateTime.now();
            Path logFile = getLogFile(now);
            try (BufferedWriter out = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
                for (int i = 0; i < 20; i++) {
                    out.write(generateRandomString());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(10) + "\n";
    }

    private Path getLogFile(LocalDateTime date) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getFileNameDateFormat());
        String dateRef = date.minusMinutes(date.getMinute() % PERIOD).plusMinutes(PERIOD).format(formatter);
        String dateTemplateVariable = config.getFileNameDateTemplateVariable();
        Path file = Paths.get(config.getFileNameInProgressPattern().replace(dateTemplateVariable, dateRef));
        if (Files.notExists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
            renamePreviousFile(date, formatter);
        }
        return file;
    }

    private void renamePreviousFile(LocalDateTime date, DateTimeFormatter formatter) throws IOException {
        String previousDateRef = date.minusMinutes(date.getMinute() % PERIOD).format(formatter);
        String fileNameInProgressPattern = config.getFileNameInProgressPattern();
        String dateTemplateVariable = config.getFileNameDateTemplateVariable();
        Path previousFile = Paths.get(fileNameInProgressPattern.replace(dateTemplateVariable, previousDateRef));
        if (Files.exists(previousFile)) {
            String finalFileNamePattern = config.getFinalFileNamePattern();
            Path newFilePath = Paths.get(finalFileNamePattern.replace(dateTemplateVariable, previousDateRef));
            Files.move(previousFile, newFilePath);
        }
    }

}
