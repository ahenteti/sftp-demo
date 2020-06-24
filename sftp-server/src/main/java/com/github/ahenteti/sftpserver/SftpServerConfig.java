package com.github.ahenteti.sftpserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sftp.server")
public class SftpServerConfig {
    private Integer port = 22;
}
