package com.github.ahenteti.sftpserver;

import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.common.util.security.bouncycastle.BouncyCastleGeneratorHostKeyProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@Component
public class SftpServer {

    @Autowired
    private SftpServerConfig config;

    public void start() throws IOException {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(config.getPort());
        sshServer.setKeyPairProvider(new BouncyCastleGeneratorHostKeyProvider(getKeyStore()));
        sshServer.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
        sshServer.setPasswordAuthenticator(AcceptAllPasswordAuthenticator.INSTANCE);
        sshServer.setCommandFactory(new ScpCommandFactory());
        sshServer.start();
    }

    private Path getKeyStore() throws IOException {
        // code inspiration: https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
        Path keystore = Files.createTempFile("keystore", ".tmp");
        IoUtils.copy(new ClassPathResource("keystore").getInputStream(), Files.newOutputStream(keystore));
        return keystore;
    }
}
