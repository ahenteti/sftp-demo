package com.github.ahenteti.sftpserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("file.generator")
public class FileGeneratorConfig {
    private String fileNameInProgressPattern = "target/folder/prefix_${datetime}";
    private String finalFileNamePattern = "target/folder/prefix_${datetime}.txt";
    private String fileNameDateFormat = "yyyy-MM-dd_HHmm";
    private String fileNameDateTemplateVariable = "${datetime}";
    
}
