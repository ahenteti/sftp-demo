package com.github.ahenteti.sftpclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sftp.client")
public class SftpClientConfig {
    private String localFileToUpload = "sftp.upload.txt";
    private String localFileToDownload = "sftp.download.txt";
    private String remoteFile = "sftp.txt";
    private String remoteHost = "localhost";
    private String username = "bob";
    private String password = "pass";
    private String strictHostKeyChecking = "no";
}
