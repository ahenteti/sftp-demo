package com.github.ahenteti.sftpclient;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SftpClient {

    private SftpClientConfig config;
    private Scanner scanner;

    public SftpClient(SftpClientConfig config, Scanner scanner) {
        this.config = config;
        this.scanner = scanner;
    }

    @Autowired
    public SftpClient(SftpClientConfig config) {
        this(config, new Scanner(System.in));
    }

    public void run() throws Exception {
        System.out.println("uploading " + config.getLocalFileToUpload() + " file...");
        upload();
        System.out.println("press enter key to download that file");
        scanner.nextLine();
        System.out.println("downloading " + config.getRemoteFile() + " file from SFTP server...");
        download();
    }

    public void upload() throws Exception {
        try (SftpChannelAutoClosable channelSftp = setupJsch()) {
            channelSftp.connect();
            channelSftp.put(config.getLocalFileToUpload(), config.getRemoteFile());
        }
    }

    public void download() throws Exception {
        try (SftpChannelAutoClosable channelSftp = setupJsch()) {
            channelSftp.connect();
            channelSftp.get(config.getRemoteFile(), config.getLocalFileToDownload());
        }
    }

    private SftpChannelAutoClosable setupJsch() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(config.getUsername(), config.getRemoteHost());
        session.setPassword(config.getPassword());
        session.setConfig("StrictHostKeyChecking", config.getStrictHostKeyChecking());
        session.connect();
        return new SftpChannelAutoClosable((ChannelSftp) session.openChannel("sftp"));
    }

}
