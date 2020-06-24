package com.github.ahenteti.sftpclient;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SftpChannelAutoClosable implements AutoCloseable {
    private ChannelSftp sftpChannel;

    public SftpChannelAutoClosable(ChannelSftp channelSftp) {
        this.sftpChannel = channelSftp;
    }

    @Override
    public void close() {
        sftpChannel.exit();
    }

    public void connect() throws JSchException {
        sftpChannel.connect();
    }

    public void put(String src, String dst) throws SftpException {
        sftpChannel.put(src, dst);
    }

    public void get(String src, String dst) throws SftpException {
        sftpChannel.get(src, dst);
    }
}
